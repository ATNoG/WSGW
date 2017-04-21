package pt.ua.it.atnog.wsGw;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.ua.it.atnog.wsGw.task.*;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class WSConn extends WebSocketAdapter {
    private BlockingQueue<Task> queue;
    private boolean open;

    public WSConn(BlockingQueue<Task> queue) {
        this.queue = queue;
        open = true;
    }

    public void onWebSocketText(String message) {
        try {
            JSONObject json = JSONObject.read(message);
            String type = json.get("type").asString();
            switch (type) {
                case "topics":
                    queue.put(new TaskTopics(this));
                    break;
                case "sub":
                    queue.put(new TaskSub(json.get("topic").asString(), this));
                    break;
                case "unsub":
                    queue.put(new TaskUnSub(json.get("topic").asString(), this));
            }
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
            queue.put(new TaskUnSubAll(this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onWebSocketClose(statusCode, reason);
    }
}
