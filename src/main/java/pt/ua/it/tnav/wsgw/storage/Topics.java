package pt.ua.it.tnav.wsgw.storage;

import pt.it.av.tnav.utils.json.JSONObject;
import pt.ua.it.tnav.wsgw.Conn;

import java.util.List;

/**
 * Topics interface.
 * <p>
 * Minimal functionality necessary to manage topics subscriptions.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public interface Topics {
  /**
   * Stores and sends data to the respective topic queue and subscribers.
   *
   * @param topic {@link String} that represents a topic.
   * @param data  {@link JSONObject} that contains the the data.
   */
  void notify(String topic, JSONObject data);

  /**
   * Adds a new subscriber to the specific topic.
   *
   * @param topic {@link String} that represents a topic.
   * @param conn  {@link Conn} that represents and web-socket connection.
   */
  void register(String topic, Conn conn);

  /**
   * Unsubscribe an entity from a specific topic.
   *
   * @param topic {@link String} that represents a topic.
   * @param conn  {@link Conn} that represents and web-socket connection.
   */
  void unsubscribe(String topic, Conn conn);

  /**
   * Unsubscribe an entity from all topics.
   *
   * @param conn {@link Conn} that represents and web-socket connection.
   */
  void unsubscribe(Conn conn);

  /**
   * List all available topics.
   *
   * @return {@link List} with all possible topics.
   */
  List<String> keys();

  /**
   * Returns an {@link JSONObject} document with the current state of the storage.
   *
   * @return {@link JSONObject} document with the current state of the storage.
   */
  JSONObject status();
}
