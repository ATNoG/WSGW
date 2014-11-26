package pt.ua.it.atnog.wsGw;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import pt.ua.it.atnog.wsGw.task.*;

import java.util.concurrent.BlockingQueue;

public class PubSubManager implements Runnable {
    private BlockingQueue<Task> queue;
    private boolean done;
    private Storage storage;

    public PubSubManager(BlockingQueue<Task> queue) {
        this.queue = queue;
        this.storage = new Storage();
        this.done = false;
    }

    public void run() {
        while (!done) {
            try {
                Task t = queue.take();
                switch (t.type) {
                    case "pub": {
                        TaskPub task = (TaskPub) t;
                        JsonObject json = JsonObject.readFrom(task.data);
                        storage.add(json.get("topic").asString(), task.data);
                        break;
                    }
                    case "sub":
                        storage.add(((TaskSub) t).topic, ((TaskSub) t).conn);
                        break;
                    case "unsub":
                        storage.remove(((TaskUnSub) t).topic, ((TaskUnSub) t).conn);
                        break;
                    case "unsuball":
                        storage.remove(((TaskUnSubAll) t).conn);
                        break;
                    case "topics": {
                        TaskTopics task = (TaskTopics) t;
                        JsonArray array = new JsonArray();
                        for (String topic : storage.keys())
                            array.add(topic);
                        JsonObject json = new JsonObject();
                        json.add("topics", array);
                        task.conn.sendString(json.toString());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                done = true;
            }
        }
    }
}
