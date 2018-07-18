package pt.ua.it.tnav.wsgw;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.tnav.utils.Utils;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.ua.it.tnav.wsgw.task.Task;
import pt.ua.it.tnav.wsgw.task.TaskPub;
import pt.ua.it.tnav.wsgw.task.TaskSub;
import pt.ua.it.tnav.wsgw.task.TaskTopics;
import pt.ua.it.tnav.wsgw.task.TaskUnsub;
import pt.ua.it.tnav.wsgw.task.TaskUnsuball;

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
public class WsConn extends WebSocketAdapter implements Conn {
  private final Logger logger = LoggerFactory.getLogger(WsConn.class);
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
        case "pub":
          queue.put(new TaskPub(json));
          break;
        case "sub":
          queue.put(new TaskSub(json.get("topic").asString(), this));
          break;
        case "unsub":
          queue.put(new TaskUnsub(json.get("topic").asString(), this));
          break;
        case "unsuball":
          queue.put(new TaskUnsuball(this));
          break;
        case "topics":
          queue.put(new TaskTopics(this));
          break;
        case "shutdown":
          break;
        default:
          logger.warn("Unknown task: " + type);
          break;
      }
    } catch (InterruptedException e) {
      logger.error(Utils.stackTrace(e));
    } catch (IOException e) {
      logger.error(Utils.stackTrace(e));
      logger.warn("Skip WS packet.");
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
        logger.error(Utils.stackTrace(e));
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
      logger.error(Utils.stackTrace(e));
    }
    super.onWebSocketClose(statusCode, reason);
  }

  @Override
  public String toString() {
    return getSession().getRemoteAddress() + "; Is Connected: " + isConnected();
  }
}