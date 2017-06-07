package pt.ua.it.atnog.wsgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.atnog.utils.Utils;
import pt.ua.it.atnog.wsgw.task.Task;
import pt.ua.it.atnog.wsgw.task.TaskUnsuball;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;

/**
 * UdpConn class.
 * <p>
 * Helper class used to interact with udp entities.
 * Provides methods to send data through udp connections.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class UdpConn implements Conn, AutoCloseable {
  private final Logger logger = LoggerFactory.getLogger(UdpConn.class);
  private final SocketAddress address;
  private final DatagramSocket socket;
  private final BlockingQueue<Task> queue;

  /**
   * @param queue
   * @param address
   * @param socket
   */
  public UdpConn(final BlockingQueue<Task> queue, final SocketAddress address,
                 final DatagramSocket socket) {
    this.queue = queue;
    this.address = address;
    this.socket = socket;
  }

  @Override
  public void sendString(String txt) {
    try {
      byte data[] = txt.getBytes();
      DatagramPacket dp = new DatagramPacket(data, data.length, address);
      socket.send(dp);
    } catch (IOException e) {
      logger.error(Utils.stackTrace(e));
      close();
    }
  }

  @Override
  public void close() {
    try {
      queue.put(new TaskUnsuball(this));
    } catch (InterruptedException e) {
      logger.error(Utils.stackTrace(e));
    }
  }
}
