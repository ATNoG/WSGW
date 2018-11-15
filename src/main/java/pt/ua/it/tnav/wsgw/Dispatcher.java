package pt.ua.it.tnav.wsgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.tnav.utils.Utils;
import pt.it.av.tnav.utils.json.JSONArray;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.ua.it.tnav.wsgw.storage.NoStorage;
import pt.ua.it.tnav.wsgw.storage.Storage;
import pt.ua.it.tnav.wsgw.storage.Topics;
import pt.ua.it.tnav.wsgw.task.Task;
import pt.ua.it.tnav.wsgw.task.TaskPub;
import pt.ua.it.tnav.wsgw.task.TaskShutdown;
import pt.ua.it.tnav.wsgw.task.TaskStatus;
import pt.ua.it.tnav.wsgw.task.TaskSub;
import pt.ua.it.tnav.wsgw.task.TaskTopics;
import pt.ua.it.tnav.wsgw.task.TaskUnsub;
import pt.ua.it.tnav.wsgw.task.TaskUnsuball;

import java.time.Instant;
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
  private final Topics topics;
  private boolean done;


  /**
   * Dispatcher constructor.
   * Constructs an dispatcher with a specific blocking queue.
   *
   * @param queue {@link BlockingQueue} used to receive tasks.
   * @param qSize maximum number of values in the {@link Storage}
   */
  public Dispatcher(final BlockingQueue<Task> queue, final int qSize) {
    this.queue = queue;
    if (qSize > 0) {
      topics = new Storage(qSize);
    } else {
      topics = new NoStorage();
    }
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
        logger.info(task.toString());
      } catch (InterruptedException e) {
        logger.error(Utils.stackTrace(e));
        task = new TaskShutdown();
      }

      switch (task.type()) {
        case "pub": {
          TaskPub taskp = (TaskPub) task;
          if (!taskp.data().contains("ts")) {
            taskp.data().put("ts", Instant.now().toEpochMilli() / 1000);
          }
          topics.notify(taskp.data().get("topic").asString(), taskp.data());
          break;
        }
        case "sub":
          topics.register(((TaskSub) task).topic(), ((TaskSub) task).conn());
          break;
        case "unsub":
          topics.unsubscribe(((TaskUnsub) task).topic(), ((TaskUnsub) task).conn());
          break;
        case "unsuball":
          topics.unsubscribe(((TaskUnsuball) task).conn());
          break;
        case "releaseall":
          topics.releaseall();
          break;
        case "topics": {
          TaskTopics taskt = (TaskTopics) task;
          JSONArray array = new JSONArray();
          for (String topic : topics.keys()) {
            array.add(topic);
          }
          JSONObject json = new JSONObject();
          json.put("topics", array);
          taskt.conn().sendJSON(json);
          break;
        }
        case "status": {
          TaskStatus tasks = (TaskStatus) task;
          tasks.conn().sendJSON(topics.status());
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
