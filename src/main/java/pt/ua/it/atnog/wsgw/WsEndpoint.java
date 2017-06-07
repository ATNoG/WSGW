package pt.ua.it.atnog.wsgw;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.atnog.utils.Utils;
import pt.ua.it.atnog.wsgw.task.Task;

import java.util.concurrent.BlockingQueue;

/**
 * WsEndpoint class.
 * <p>
 * Helper class to interact with web-socket connections.
 * Setups the {@link WsServlet} to interact with web-sockets.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class WsEndpoint {
  private final Logger logger = LoggerFactory.getLogger(WsEndpoint.class);
  private final Server server;

  /**
   * WsEndpoint constructor.
   * Constructors a WsEndpoint with a specific {@link BlockingQueue} and port.
   *
   * @param queue {@link BlockingQueue} used to send tasks.
   * @param port  port used to communicate with the gateway.
   */
  public WsEndpoint(BlockingQueue<Task> queue, int port) {
    server = new Server(port);
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.addServlet(new ServletHolder(new WsServlet(queue)), "/pubsub/");
    server.setHandler(context);
  }

  /**
   * Starts the WsEndpoint thread.
   */
  public void start() {
    try {
      server.start();
      logger.info("WS Endpoint running.");
    } catch (Exception e) {
      logger.error(Utils.stackTrace(e));
    }
  }

  /**
   * Joins the WsEndpoint thread.
   * Implements a graceful shutdown.
   */
  public void join() {
    try {
      server.stop();
      server.join();
      logger.info("WS Endpoint joined.");
    } catch (Exception e) {
      logger.error(Utils.stackTrace(e));
    }
  }
}
