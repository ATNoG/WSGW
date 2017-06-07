package pt.ua.it.atnog.wsgw;

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
}
