package pt.ua.it.atnog.wsGw;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import pt.ua.it.atnog.wsGw.task.Task;

import java.util.concurrent.BlockingQueue;

public class WSConnCreator implements WebSocketCreator {
    private BlockingQueue<Task> queue;

    public WSConnCreator(BlockingQueue<Task> queue) {
        this.queue = queue;
    }

    public Object createWebSocket(ServletUpgradeRequest arg0,
                                  ServletUpgradeResponse arg1) {
        return new WSConn(queue);
    }
}
