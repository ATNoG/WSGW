package pt.ua.it.atnog.wsgw;

import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import pt.ua.it.atnog.wsgw.task.Task;

import java.util.concurrent.BlockingQueue;

/**
 * WsServlet class.
 * <p>
 * Extends {@link WebSocketServlet}, it allows define a {@link WebSocketCreator}.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class WsServlet extends WebSocketServlet {
  private static final long serialVersionUID = 2109739803483163234L;
  private BlockingQueue<Task> queue;

  /**
   * WsServlet constructor.
   * Constructors a WsServlet with a specific {@link BlockingQueue}.
   *
   * @param queue {@link BlockingQueue} used to send tasks.
   */
  public WsServlet(BlockingQueue<Task> queue) {
    super();
    this.queue = queue;
  }

  @Override
  public void configure(WebSocketServletFactory factory) {
    factory.setCreator(new WsConncreator(queue));
  }
}
