package pt.ua.it.atnog.wsgw.task;

import pt.ua.it.atnog.wsgw.Conn;

/**
 * TaskUnsuball class.
 * Task used when an entity un-subscribe all topics.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskUnsuball extends Task {
  private final Conn conn;

  /**
   * TaskUnsub constructor.
   * Construct a TaskUnsuball with a specific web-socket.
   *
   * @param conn web-socket connection of the entity.
   */
  public TaskUnsuball(Conn conn) {
    super("unsuball");
    this.conn = conn;
  }

  /**
   * Returns the web-socket connection of an entity.
   *
   * @return the web-socket connection of an entity.
   */
  public Conn conn() {
    return conn;
  }
}
