package pt.ua.it.atnog.wsgw;

import pt.it.av.atnog.utils.json.JSONObject;
import pt.it.av.atnog.utils.structures.CircularQueue;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Storage class.
 * <p>
 * Extended {@link HashMap} to support minimal gateway storage capabilities.
 * The topics are the keys, and the data is stored in {@link CircularQueue} with finite space.
 * This allows the storage to save the most recent N
 * elements while providing good storage performance.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Storage extends HashMap<String, Pair<List<WsConn>, CircularQueue<JSONObject>>> {
  private final int qSize;

  /**
   * Storage constructor.
   * Constructs a {@link Storage} and limits the size of the {@link CircularQueue} to qSize.
   *
   * @param qSize maximum number of values in the {@link CircularQueue}.
   */
  public Storage(final int qSize) {
    this.qSize = qSize;
  }

  /**
   * Insert data related to a topic in the storage.
   *
   * @param topic {@link String} that represents a topic.
   * @param data  {@link JSONObject} that contains the the data.
   */
  public void put(String topic, JSONObject data) {
    Pair<List<WsConn>, CircularQueue<JSONObject>> item = null;
    if (containsKey(topic)) {
      item = get(topic);
      for (WsConn c : item.a) {
        try {
          c.getRemote().sendString(data.toString());
        } catch (Exception e) {
          e.printStackTrace();
          item.a.remove(c);
        }
      }
    } else {
      item = new Pair<>(new ArrayList<WsConn>(), new CircularQueue<JSONObject>(qSize));
      put(topic, item);
    }
    item.b.add(data);
  }

  /**
   * Insert a new subscriber to the specific topic.
   *
   * @param topic {@link String} that represents a topic.
   * @param conn  {@link WsConn} that represents and web-socket connection.
   */
  public void put(String topic, WsConn conn) {
    Pair<List<WsConn>, CircularQueue<JSONObject>> item = null;
    if (containsKey(topic)) {
      item = get(topic);
      for (JSONObject datum : item.b) {
        try {
          conn.remote().sendString(datum.toString());
        } catch (Exception e) {
          e.printStackTrace();
          item.a.remove(conn);
        }
      }
    } else {
      item = new Pair<>(new ArrayList<WsConn>(), new CircularQueue<JSONObject>());
      put(topic, item);
    }
    item.a.add(conn);
  }

  /**
   * Un-subscribes an entity from a specific topic.
   *
   * @param topic {@link String} that represents a topic.
   * @param conn  {@link WsConn} that represents and web-socket connection.
   */
  public void remove(String topic, WsConn conn) {
    if (containsKey(topic)) {
      get(topic).a.remove(conn);
    }
  }

  /**
   * Un-subscribes an entity from all topics.
   *
   * @param conn {@link WsConn} that represents and web-socket connection.
   */
  public void remove(WsConn conn) {
    for (Map.Entry<String, Pair<List<WsConn>, CircularQueue<JSONObject>>> entry : entrySet()) {
      entry.getValue().a.remove(conn);
    }
  }

  /**
   * List all available topics.
   *
   * @return {@link List} with all possible topics.
   */
  public List<String> keys() {
    return new ArrayList<String>(keySet());
  }
}
