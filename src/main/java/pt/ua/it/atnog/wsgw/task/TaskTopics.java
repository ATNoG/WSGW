package pt.ua.it.atnog.wsgw.task;

import pt.ua.it.atnog.wsgw.Conn;

/**
 * TaskTopics class.
 * Task used when an entity lists all the available topics.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskTopics extends Task {
  private final Conn conn;

  /**
   * TaskTopics constructor.
   * Constructs a TaskTopics for a specific entity.
   *
   * @param conn web-socket connection of the entity.
   */
  public TaskTopics(Conn conn) {
    super("topics");
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
