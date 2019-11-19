package pt.ua.it.tnav.wsgw;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import pt.ua.it.tnav.wsgw.task.Task;

import java.util.concurrent.BlockingQueue;

/**
 * WsConncreator class.
 * <p>
 * Implements {@link WebSocketCreator}, it allows to pass a {@link BlockingQueue}
 * to the {@link WsConn} constructor.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class WsConncreator implements WebSocketCreator {
  private BlockingQueue<Task> queue;

  /**
   * WsConncreator constructor.
   * Constructors a WsConncreator with a specific {@link BlockingQueue}.
   *
   * @param queue {@link BlockingQueue} used to send tasks.
   */
  public WsConncreator(BlockingQueue<Task> queue) {
    this.queue = queue;
  }

  @Override
  public Object createWebSocket(ServletUpgradeRequest arg0,
                                ServletUpgradeResponse arg1) {
    return new WsConn(queue);
  }
}
