package pt.ua.it.tnav.wsgw.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.ua.it.tnav.wsgw.Conn;
import pt.ua.it.tnav.wsgw.WsConn;

/**
 *
 */
public class TaskFactory {
  private static final Logger logger = LoggerFactory.getLogger(WsConn.class);
  private TaskFactory(){};

  public static Task build(JSONObject json, Conn conn) {
    Task t = null;

    String type = json.get("type").asString();
    switch (type) {
      case "pub":
        t = new TaskPub(json);
        break;
      case "sub":
        t = new TaskSub(json.get("topic").asString(), conn);
        break;
      case "unsub":
        t = new TaskUnsub(json.get("topic").asString(), conn);
        break;
      case "unsuball":
        t = new TaskUnsuball(conn);
        break;
      case "topics":
        t = new TaskTopics(conn);
        break;
      case "status":
        t = new TaskStatus(conn);
        break;
      case "shutdown":
        t = new TaskShutdown();
        break;
      default:
        logger.warn("Unknown task: " + type);
        break;
    }
    return t;
  }
}
