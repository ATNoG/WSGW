package pt.ua.it.atnog.wsgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.json.JSONArray;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.ua.it.atnog.wsgw.task.Task;
import pt.ua.it.atnog.wsgw.task.TaskPub;
import pt.ua.it.atnog.wsgw.task.TaskShutdown;
import pt.ua.it.atnog.wsgw.task.TaskSub;
import pt.ua.it.atnog.wsgw.task.TaskTopics;
import pt.ua.it.atnog.wsgw.task.TaskUnsub;
import pt.ua.it.atnog.wsgw.task.TaskUnsuball;

import java.util.concurrent.BlockingQueue;

/**
 * Dispatcher class.
 * <p>
 * Implements a rather simple pub/sub gateway.
 * Bridges udp publishers with web-socket subscribers.
 * Receives {@link Task} from a {@link BlockingQueue} ane executes them.
 * Relies on {@link Storage} to store a limited number of values.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Dispatcher implements Runnable {
  private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
  private final Thread thread;
  private final BlockingQueue<Task> queue;
  private final Storage storage;
  private boolean done;


  /**
   * Dispatcher constructor.
   * Constructs an dispatcher with a specific blocking queue.
   *
   * @param queue {@link BlockingQueue} used to receive tasks.
   */
  public Dispatcher(final BlockingQueue<Task> queue, final int qSize) {
    this.queue = queue;
    storage = new Storage(qSize);
    thread = new Thread(this);
    done = false;
  }

  /**
   * Starts the dispatcher thread.
   */
  public void start() {
    logger.info("Dispatcher running.");
    thread.start();
  }

  /**
   * Joins the dispatcher thread.
   * Implements a graceful shutdown.
   */
  public void join() {
    try {
      thread.join();
      logger.info("Dispatcher joined.");
    } catch (InterruptedException e) {
      logger.error(Utils.stackTrace(e));
    }
  }

  @Override
  public void run() {
    while (!done) {
      Task task;
      try {
        task = queue.take();
        logger.trace(task.toString());
      } catch (InterruptedException e) {
        logger.error(Utils.stackTrace(e));
        task = new TaskShutdown();
      }

      switch (task.type()) {
        case "pub": {
          TaskPub taskp = (TaskPub) task;
          storage.put(taskp.data().get("topic").asString(), taskp.data());
          break;
        }
        case "sub":
          storage.put(((TaskSub) task).topic(), ((TaskSub) task).conn());
          break;
        case "unsub":
          storage.remove(((TaskUnsub) task).topic(), ((TaskUnsub) task).conn());
          break;
        case "unsuball":
          storage.remove(((TaskUnsuball) task).conn());
          break;
        case "topics": {
          TaskTopics taskt = (TaskTopics) task;
          JSONArray array = new JSONArray();
          for (String topic : storage.keys()) {
            array.add(topic);
          }
          JSONObject json = new JSONObject();
          json.put("topics", array);
          taskt.conn().sendString(json.toString());
          break;
        }
        case "shutdown": {
          done = true;
          break;
        }
        default: {
          logger.warn("Unknown task: " + task.type());
          break;
        }
      }
    }
  }
}
