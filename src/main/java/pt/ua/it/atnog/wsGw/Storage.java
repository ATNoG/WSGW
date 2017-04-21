package pt.ua.it.atnog.wsGw;

import pt.it.av.atnog.utils.json.JSONObject;
import pt.it.av.atnog.utils.structures.CircularQueue;
import pt.it.av.atnog.utils.structures.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage extends HashMap<String, Pair<List<WSConn>, CircularQueue<JSONObject>>> {

    /**
     * @param topic
     * @param data
     */
    public void put(String topic, JSONObject data) {
        Pair<List<WSConn>, CircularQueue<JSONObject>> item = null;
        if (containsKey(topic)) {
            item = get(topic);
            for (WSConn c : item.a) {
                try {
                    c.getRemote().sendString(data.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    item.a.remove(c);
                }
            }
        } else {
            item = new Pair<>(new ArrayList<WSConn>(), new CircularQueue<JSONObject>());
            put(topic, item);
        }
        item.b.add(data);
    }

    public void put(String topic, WSConn conn) {
        Pair<List<WSConn>, CircularQueue<JSONObject>> item = null;
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
            item = new Pair<>(new ArrayList<WSConn>(), new CircularQueue<JSONObject>());
            put(topic, item);
        }
        item.a.add(conn);
    }

    public void remove(String topic, WSConn conn) {
        if (containsKey(topic))
            get(topic).a.remove(conn);
    }

    public void remove(WSConn conn) {
        for (Map.Entry<String, Pair<List<WSConn>, CircularQueue<JSONObject>>> entry : entrySet()) {
            entry.getValue().a.remove(conn);
        }
    }

    public List<String> keys() {
        return new ArrayList<String>(keySet());
    }
}
