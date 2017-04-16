package pt.ua.it.atnog.wsGw;

import pt.it.av.atnog.utils.structures.CircularQueue;

import java.util.*;

public class Storage {
    private final Map<String, Item> map;

    public Storage() {
        this.map = new HashMap<String, Item>();
    }

    public void add(String topic, String data) {
        Item item = null;
        if (map.containsKey(topic)) {
            item = map.get(topic);
            for (WSConn c : item.conns) {
                try {
                    c.getRemote().sendString(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    item.conns.remove(c);
                }
            }
        } else {
            item = new Item();
            map.put(topic, item);
        }
        item.buffer.add(data);
    }

    public void add(String topic, WSConn conn) {
        Item item = null;
        if (map.containsKey(topic)) {
            item = map.get(topic);
            for (String datum : item.buffer) {
                try {
                    conn.remote().sendString(datum);
                } catch (Exception e) {
                    e.printStackTrace();
                    item.conns.remove(conn);
                }
            }
        } else {
            item = new Item();
            map.put(topic, item);
        }
        item.conns.add(conn);
    }

    public void remove(String topic, WSConn conn) {
        if (map.containsKey(topic)) {
            map.get(topic).conns.remove(conn);
        }
    }

    public void remove(WSConn conn) {
        for (Map.Entry<String, Item> entry : map.entrySet()) {
            entry.getValue().conns.remove(conn);
        }
    }

    public List<String> keys() {
        return new ArrayList<String>(map.keySet());
    }

    private class Item {
        public List<WSConn> conns = new LinkedList<WSConn>();
        public CircularQueue<String> buffer = new CircularQueue<String>();
    }
}
