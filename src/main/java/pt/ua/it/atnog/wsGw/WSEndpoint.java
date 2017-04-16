package pt.ua.it.atnog.wsGw;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pt.ua.it.atnog.wsGw.task.Task;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;


public class WSEndpoint {
    private Server server;

    public WSEndpoint(BlockingQueue<Task> queue, int port) {
        Properties p = new Properties();
        p.setProperty("org.eclipse.jetty.LEVEL", "WARN");
        org.eclipse.jetty.util.log.StdErrLog.setProperties(p);
        server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new WSServlet(queue)), "/pubsub/");
        server.setHandler(context);
    }

    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void join() {
        try {
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
