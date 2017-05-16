package pt.ua.it.atnog.wsgw;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.ua.it.atnog.wsgw.task.Task;
import pt.ua.it.atnog.wsgw.task.TaskSub;
import pt.ua.it.atnog.wsgw.task.TaskTopics;
import pt.ua.it.atnog.wsgw.task.TaskUnsub;
import pt.ua.it.atnog.wsgw.task.TaskUnsuball;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * WsConn class.
 * <p>
 * Helper class used to interact with web-socket entities.
 * Provides methods to receive and send data through web-sockets.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class WsConn extends WebSocketAdapter {
  private final Logger logger = LoggerFactory.getLogger(Gateway.class);
  private BlockingQueue<Task> queue;
  private boolean open;

  /**
   * WsConn constructor.
   * Constructs a web-socket connection with a specific {@link BlockingQueue}.
   *
   * @param queue {@link BlockingQueue} used to receive tasks.
   */
  public WsConn(BlockingQueue<Task> queue) {
    this.queue = queue;
    open = true;
  }

  @Override
  public void onWebSocketText(String message) {
    try {
      JSONObject json = JSONObject.read(message);
      String type = json.get("type").asString();
      switch (type) {
        case "topics":
          queue.put(new TaskTopics(this));
          break;
        case "sub":
          queue.put(new TaskSub(json.get("topic").asString(), this));
          break;
        case "unsub":
          queue.put(new TaskUnsub(json.get("topic").asString(), this));
          break;
        default:
          logger.warn("Unknown task: " + type);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the {@link RemoteEndpoint} associated with the {@link WsConn}.
   *
   * @return {@link RemoteEndpoint} associated with the {@link WsConn}.
   */
  public RemoteEndpoint remote() {
    return getSession().getRemote();
  }

  /**
   * Sends a {@link String} message to the entity.
   *
   * @param txt {@link String} message.
   */
  public void sendString(String txt) {
    if (open) {
      try {
        remote().sendString(txt);
      } catch (IOException e) {
        e.printStackTrace();
        onWebSocketClose(0, null);
      }
    }
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    open = false;
    try {
      queue.put(new TaskUnsuball(this));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    super.onWebSocketClose(statusCode, reason);
  }
}