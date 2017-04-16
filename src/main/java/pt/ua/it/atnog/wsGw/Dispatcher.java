package pt.ua.it.atnog.wsGw;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import pt.ua.it.atnog.wsGw.task.*;

import java.util.concurrent.BlockingQueue;

public class Dispatcher implements Runnable {
    private final Thread t;
    private final BlockingQueue<Task> queue;
    private final Storage storage;
    private boolean done;


    public Dispatcher(BlockingQueue<Task> queue) {
        this.queue = queue;
        storage = new Storage();
        t = new Thread(this);
        done = false;
    }

    public void start() {
        t.start();
    }

    public void join() {
        try {
            queue.put(new TaskShutdown());
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!done) {
            try {
                Task t = queue.take();
                switch (t.type()) {
                    case "pub": {
                        TaskPub task = (TaskPub) t;
                        JsonObject json = JsonObject.readFrom(task.data);
                        System.out.println(json);
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
                    case "shutdown": {
                        done = true;
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
