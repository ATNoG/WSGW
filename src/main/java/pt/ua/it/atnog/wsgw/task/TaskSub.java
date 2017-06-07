package pt.ua.it.atnog.wsgw.task;

import pt.ua.it.atnog.wsgw.Conn;

/**
 * TaskSub class.
 * Task used when an entity subscribe data from the gateway.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskSub extends Task {
  private final Conn conn;
  private final String topic;

  /**
   * TaskSub constructor.
   * Construct a TaskSub with a specific topic and wsconn.
   *
   * @param topic Topic subscribed by an entity.
   * @param conn Connection of the entity.
   */
  public TaskSub(String topic, Conn conn) {
    super("sub");
    this.topic = topic;
    this.conn = conn;
  }

  /**
   * Returns the topic subscribed by and entity.
   *
   * @return the topic subscribed by and entity.
   */
  public String topic() {
    return topic;
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
