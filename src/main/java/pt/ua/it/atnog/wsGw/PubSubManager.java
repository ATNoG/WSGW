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
                Task task = queue.take();
                switch (task.type) {
                    case "pub": {
                        TaskPub msg = (TaskPub) task;
                        System.err.println(msg.data);
                        JsonObject json = JsonObject.readFrom(msg.data);
                        String topic = json.get("topic").asString();
                        storage.add(topic, msg.data);
                        break;
                    }
                    case "sub":
                        storage.add(((TaskSub) task).topic, ((TaskSub) task).conn);
                        break;
                    case "unsub":
                        storage.remove(((TaskUnSub) task).topic, ((TaskUnSub) task).conn);
                        break;
                    case "unsuball":
                        storage.remove(((TaskUnSub) task).conn);
                        break;
                    case "topics": {
                        TaskTopics msg = (TaskTopics) task;
                        JsonObject json = new JsonObject();
                        JsonArray array = new JsonArray();
                        for (String topic : storage.keys())
                            array.add(topic);
                        json.add("topics", array);
                        System.err.println(json);
                        msg.conn.sendString(json.toString());
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
