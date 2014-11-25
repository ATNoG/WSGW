package pt.ua.it.atnog.wsGw;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import pt.ua.it.atnog.wsGw.task.Task;

import java.util.concurrent.BlockingQueue;

public class WSServlet extends WebSocketServlet {
    private static final long serialVersionUID = 2109739803483163234L;
    private BlockingQueue<Task> queue;

    public WSServlet(BlockingQueue<Task> queue) {
        super();
        this.queue = queue;
    }

    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(new WSConnCreator(queue));
    }
}
