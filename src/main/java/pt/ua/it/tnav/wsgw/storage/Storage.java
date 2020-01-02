package pt.ua.it.tnav.wsgw.storage;

import pt.it.av.tnav.utils.json.JSONArray;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.it.av.tnav.utils.structures.queue.CircularPriorityQueue;
import pt.it.av.tnav.utils.structures.tuple.Pair;
import pt.ua.it.tnav.wsgw.Conn;

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
public class Storage extends HashMap<String, Pair<List<Conn>, Queue<JSONObject>>> implements Topics {
  private static final long serialVersionUID = 1L;
  private final int qSize;
  private final Comparator<JSONObject> c =
      (JSONObject o1, JSONObject o2) -> Long.compare(o1.get("ts").asLong(), o2.get("ts").asLong());

  /**
   * Storage constructor.
   * <p>
   * Constructs a {@link Storage} and limits the size of the {@link CircularPriorityQueue} to qSize.
   * </p>
   *
   * @param qSize maximum number of values in the {@link CircularPriorityQueue}.
   */
  public Storage(final int qSize) {
    super();
    this.qSize = qSize;
  }

  @Override
  public void notify(String topic, JSONObject data) {
    Pair<List<Conn>, Queue<JSONObject>> item;
    if (containsKey(topic)) {
      item = get(topic);
    } else {
      item = new Pair<>(new ArrayList<>(), new CircularPriorityQueue<>(qSize, c));
      put(topic, item);
    }
    if (item.b.add(data)) {
      for (Conn c : item.a) {
        c.sendJSON(data);
      }
    }
  }

  @Override
  public void register(String topic, Conn conn) {
    Pair<List<Conn>, Queue<JSONObject>> item;
    if (containsKey(topic)) {
      item = get(topic);
      for (JSONObject datum : item.b) {
        conn.sendJSON(datum);
      }
    } else {
      item = new Pair<>(new ArrayList<>(), new CircularPriorityQueue<>(qSize, c));
      put(topic, item);
    }
    if(!item.a.contains(conn)) {
      item.a.add(conn);
    }
  }

  @Override
  public void unsubscribe(String topic, Conn conn) {
    if (containsKey(topic)) {
      get(topic).a.remove(conn);
    }
  }

  @Override
  public void unsubscribe(Conn conn) {
    for (Map.Entry<String, Pair<List<Conn>, Queue<JSONObject>>> entry : entrySet()) {
      entry.getValue().a.remove(conn);
    }
  }

  @Override
  public void releaseall() {
    this.clear();
  }

  @Override
  public List<String> keys() {
    return new ArrayList<>(keySet());
  }

  @Override
  public JSONObject status() {
    JSONObject json = new JSONObject();
    JSONArray jTopics = new JSONArray();
    List<String> topics = keys();

    for(String t :topics) {
      JSONArray conns = new JSONArray();
      JSONObject topic = new JSONObject();
      Pair<List<Conn>, Queue<JSONObject>> pair = get(t);
      topic.put("topic", t);
      topic.put("queue", pair.b.size());
      for(Conn c : pair.a) {
        conns.add(c.toString());
      }
      topic.put("connections", conns);
      jTopics.add(topic);
    }
    json.put("topics", jTopics);

    return json;
  }
}
