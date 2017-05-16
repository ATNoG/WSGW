package pt.ua.it.atnog.wsgw.task;

import pt.ua.it.atnog.wsgw.WsConn;

/**
 * TaskTopics class.
 * Task used when an entity lists all the available topics.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskTopics extends Task {
  private final WsConn wsconn;

  /**
   * TaskTopics constructor.
   * Constructs a TaskTopics for a specific entity.
   *
   * @param wsconn web-socket connection of the entity.
   */
  public TaskTopics(WsConn wsconn) {
    super("topics");
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
