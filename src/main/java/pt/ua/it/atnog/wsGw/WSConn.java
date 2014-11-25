package pt.ua.it.atnog.wsGw;

import com.eclipsesource.json.JsonObject;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import pt.ua.it.atnog.wsGw.task.Task;
import pt.ua.it.atnog.wsGw.task.TaskSub;
import pt.ua.it.atnog.wsGw.task.TaskTopics;
import pt.ua.it.atnog.wsGw.task.TaskUnSub;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class WSConn extends WebSocketAdapter {
    private BlockingQueue<Task> queue;
    private String key;
    private boolean open;

    public WSConn(BlockingQueue<Task> queue) {
        this.queue = queue;
        open = true;
    }

    public void onWebSocketText(String message) {
        try {
            System.err.println(message);
            JsonObject json = JsonObject.readFrom(message);
            String type = json.get("type").asString();
            switch (type) {
                case "topics":
                    queue.put(new TaskTopics(this));
                    break;
                case "sub":
                    queue.put(new TaskSub(json.get("topic").asString(), this));
                    break;
            }
            /*} else if (type.equals("sub")) {
                String oldKey = key;
				key = json.get("entity").asString();
				queue.put(new MsgModConn(oldKey, key, this));
			}*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RemoteEndpoint remote() {
        return getSession().getRemote();
    }

    public void sendString(String txt) {
        if (open) {
            try {
                getSession().getRemote().sendString(txt);
            } catch (IOException e) {
                e.printStackTrace();
                onWebSocketClose(0, null);
            }
        }
    }

    public void onWebSocketClose(int statusCode, String reason) {
        open = false;
        try {
            queue.put(new TaskUnSub(key, this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onWebSocketClose(statusCode, reason);
    }
}
