package pt.ua.it.atnog.wsgw.task;

import pt.ua.it.atnog.wsgw.Conn;

/**
 * TaskUnsub class.
 * Task used when an entity un-subscribe a specific topic.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskUnsub extends Task {
  private final String topic;
  private final Conn conn;

  /**
   * TaskUnsub constructor.
   * Construct a TaskUnsub with a specific topic and wsconn.
   *
   * @param topic  topic un-subscribed by an entity.
   * @param conn web-socket connection of the entity.
   */
  public TaskUnsub(String topic, Conn conn) {
    super("unsub");
    this.topic = topic;
    this.conn = conn;
  }

  /**
   * Returns the topic un-subscribe by and entity.
   *
   * @return the topic un-subscribe by and entity.
   */
  public String topic() {
    return topic;
  }

  /**
   * Returns the web-socket connection of an entity.
   *
   * @return the web-socket connection of an entity.
   */
  public Conn wsconn() {
    return conn;
  }
}
