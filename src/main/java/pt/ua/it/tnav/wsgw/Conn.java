package pt.ua.it.tnav.wsgw;

import pt.it.av.tnav.utils.json.JSONObject;

/**
 * Conn interface.
 * <p>
 * Helper interface to interact with subscriber's connections.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public interface Conn {

  /**
   * Sends a {@link String} message to the entity.
   *
   * @param txt {@link String} message.
   */
  void sendString(String txt);

  /**
   * Sends a {@link JSONObject} message to the entity.
   *
   * @param json {@link JSONObject} message.
   */
  default void sendJSON(JSONObject json) {
    sendString(json.toString());
  }
}
