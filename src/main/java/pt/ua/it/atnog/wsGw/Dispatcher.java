package pt.ua.it.atnog.wsGw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.json.JSONArray;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.ua.it.atnog.wsGw.task.*;

import java.util.concurrent.BlockingQueue;

public class Dispatcher implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
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
                        storage.put(task.data().get("topic").asString(), task.data());
                        break;
                    }
                    case "sub":
                        storage.put(((TaskSub) t).topic(), ((TaskSub) t).wsconn());
                        break;
                    case "unsub":
                        storage.remove(((TaskUnSub) t).topic(), ((TaskUnSub) t).wsconn());
                        break;
                    case "unsuball":
                        storage.remove(((TaskUnSubAll) t).wsconn());
                        break;
                    case "topics": {
                        TaskTopics task = (TaskTopics) t;
                        JSONArray array = new JSONArray();
                        for (String topic : storage.keys())
                            array.add(topic);
                        JSONObject json = new JSONObject();
                        json.put("topics", array);
                        task.wsconn().sendString(json.toString());
                        break;
                    }
                    case "shutdown": {
                        done = true;
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error(Utils.stackTrace(e));
                done = true;
            }
        }
    }
}
