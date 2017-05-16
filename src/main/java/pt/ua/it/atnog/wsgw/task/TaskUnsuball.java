package pt.ua.it.atnog.wsgw.task;

import pt.ua.it.atnog.wsgw.WsConn;

/**
 * TaskUnsuball class.
 * Task used when an entity un-subscribe all topics.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskUnsuball extends Task {
  private final WsConn wsconn;

  /**
   * TaskUnsub constructor.
   * Construct a TaskUnsuball with a specific web-socket.
   *
   * @param wsconn web-socket connection of the entity.
   */
  public TaskUnsuball(WsConn wsconn) {
    super("unsuball");
    this.wsconn = wsconn;
  }

  /**
   * Returns the web-socket connection of an entity.
   *
   * @return the web-socket connection of an entity.
   */
  public WsConn wsconn() {
    return wsconn;
  }
}
