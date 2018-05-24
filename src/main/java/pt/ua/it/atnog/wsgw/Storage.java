package pt.ua.it.atnog.wsgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.it.av.atnog.utils.structures.queue.CircularPriorityQueue;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Storage class.
 * <p>
 * Extended {@link HashMap} to support minimal gateway storage capabilities.
 * The topics are the keys, and the data is stored in {@link CircularPriorityQueue} with finite space.
 * This allows the storage to save the most recent N
 * elements while providing good storage performance.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Storage extends HashMap<String, Pair<List<Conn>, Queue<JSONObject>>> {
  private final Logger logger = LoggerFactory.getLogger(Storage.class);
  private final int qSize;
  private final Comparator<JSONObject> c =
      (JSONObject o1, JSONObject o2) -> Long.compare(o1.get("ts").asLong(), o2.get("ts").asLong());

  /**
   * Storage constructor.
   * Constructs a {@link Storage} and limits the size of the {@link CircularPriorityQueue} to qSize.
   *
   * @param qSize maximum number of values in the {@link CircularPriorityQueue}.
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

    Pair<List<Conn>, Queue<JSONObject>> item;
    if (containsKey(topic)) {
      item = get(topic);
      for (Conn c : item.a) {
        c.sendString(data.toString());
      }
    } else {
      item = new Pair<>(new ArrayList<>(), new CircularPriorityQueue<>(qSize, c));
      put(topic, item);
    }
    item.b.add(data);
  }

  /**
   * Insert a new subscriber to the specific topic.
   *
   * @param topic {@link String} that represents a topic.
   * @param conn  {@link Conn} that represents and web-socket connection.
   */
  public void put(String topic, Conn conn) {
    Pair<List<Conn>, Queue<JSONObject>> item;
    if (containsKey(topic)) {
      item = get(topic);
      for (JSONObject datum : item.b) {
        conn.sendString(datum.toString());
      }
    } else {
      item = new Pair<>(new ArrayList<>(), new CircularPriorityQueue<>(qSize, c));
      put(topic, item);
    }
    item.a.add(conn);
  }

  /**
   * Un-subscribes an entity from a specific topic.
   *
   * @param topic {@link String} that represents a topic.
   * @param conn  {@link Conn} that represents and web-socket connection.
   */
  public void remove(String topic, Conn conn) {
    if (containsKey(topic)) {
      get(topic).a.remove(conn);
    }
  }

  /**
   * Un-subscribes an entity from all topics.
   *
   * @param conn {@link Conn} that represents and web-socket connection.
   */
  public void remove(WsConn conn) {
    for (Map.Entry<String, Pair<List<Conn>, Queue<JSONObject>>> entry : entrySet()) {
      entry.getValue().a.remove(conn);
    }
  }

  /**
   * List all available topics.
   *
   * @return {@link List} with all possible topics.
   */
  public List<String> keys() {
    return new ArrayList<>(keySet());
  }
}
