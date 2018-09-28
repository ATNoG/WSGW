package pt.ua.it.tnav.wsgw.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.tnav.utils.json.JSONArray;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.ua.it.tnav.wsgw.Conn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NoStorage class.
 * <p>
 * Minimal functionality necessary to manage topics subscriptions.
 * Does not store any data.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class NoStorage extends HashMap<String, List<Conn>> implements Topics {
  private final Logger logger = LoggerFactory.getLogger(Storage.class);

  /**
   * NoStorage constructor.
   * <p>
   * Constructs a {@link NoStorage} with no storage capacity.
   * </p>
   */
  public NoStorage() {
    super();
  }

  @Override
  public void notify(String topic, JSONObject data) {
    List<Conn> conns;
    if (containsKey(topic)) {
      conns = get(topic);
      for (Conn c : conns) {
        c.sendJSON(data);
      }
    } else {
      put(topic, new ArrayList<>());
    }
  }

  @Override
  public void register(String topic, Conn conn) {
    List<Conn> conns;
    if (containsKey(topic)) {
      conns = get(topic);
    } else {
      conns = new ArrayList<>();
      put(topic, conns);
    }
    conns.add(conn);
  }

  @Override
  public void unsubscribe(String topic, Conn conn) {
    if (containsKey(topic)) {
      get(topic).remove(conn);
    }
  }

  @Override
  public void unsubscribe(Conn conn) {
    for (Map.Entry<String, List<Conn>> entry : entrySet()) {
      entry.getValue().remove(conn);
    }
  }

  @Override
  public List<String> keys() {
    return new ArrayList<>(keySet());
  }

  @Override
  public JSONObject status() {
    JSONObject json = new JSONObject();

    List<String> topics = keys();

    for(String t :topics) {
      JSONArray conns = new JSONArray();
      JSONObject topic = new JSONObject();
      List<Conn> conn = get(t);
      topic.put("topic", t);
      for(Conn c : conn) {
        conns.add(c.toString());
      }
      topic.put("connections", conns);
    }

    return json;
  }
}
