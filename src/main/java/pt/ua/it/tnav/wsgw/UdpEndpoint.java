package pt.ua.it.tnav.wsgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.tnav.utils.Utils;
import pt.it.av.tnav.utils.json.JSONObject;
import pt.ua.it.tnav.wsgw.task.Task;
import pt.ua.it.tnav.wsgw.task.TaskFactory;
import pt.ua.it.tnav.wsgw.task.TaskPub;
import pt.ua.it.tnav.wsgw.task.TaskShutdown;
import pt.ua.it.tnav.wsgw.task.TaskSub;
import pt.ua.it.tnav.wsgw.task.TaskTopics;
import pt.ua.it.tnav.wsgw.task.TaskUnsub;
import pt.ua.it.tnav.wsgw.task.TaskUnsuball;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;

/**
 * UdpEndpoint class.
 * <p>
 * Helper class to interact with udp connections.
 * Receives publish messages through udp and convert them into tasks for the {@link Dispatcher}.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class UdpEndpoint implements Runnable {
  private final Logger logger = LoggerFactory.getLogger(UdpEndpoint.class);
  private final Thread thread;
  private final BlockingQueue<Task> queue;
  private final SocketAddress address;
  private boolean done = true;

  /**
   * UdpEndpoint constructor.
   * Constructs a UdpEndpoint with a specific {@link BlockingQueue} and {@link SocketAddress}.
   *
   * @param queue   {@link BlockingQueue} used to send tasks.
   * @param address {@link SocketAddress} udp port and address.
   */
  public UdpEndpoint(BlockingQueue<Task> queue, SocketAddress address) {
    this.queue = queue;
    this.address = address;
    thread = new Thread(this);
    done = false;
  }

  /**
   * Starts the UdpEndpoint thread.
   */
  public void start() {
    thread.start();
    logger.info("UDP Endpoint running.");
  }

  /**
   * Joins the UdpEndpoint thread.
   * <p>
   * Implements a graceful shutdown.
   * </p>
   */
  public void join() {
    try {
      DatagramSocket tmpSocket = new DatagramSocket();
      byte[] data = "{\"type\":\"shutdown\"}".getBytes();
      DatagramPacket tmpPacket = new DatagramPacket(data, data.length, address);
      tmpSocket.send(tmpPacket);
      tmpSocket.close();
      thread.join();
      logger.info("UDP Endpoint joined.");
    } catch (IOException | InterruptedException e) {
      logger.error(Utils.stackTrace(e));
    }
  }

  @Override
  public void run() {
    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket(address);

      byte[] data = new byte[1024];
      DatagramPacket packet = new DatagramPacket(data, data.length);
      while (!done) {
        socket.receive(packet);
        JSONObject json = JSONObject.read(new String(
            packet.getData(), packet.getOffset(), packet.getLength()));
        Task t = TaskFactory.build(json, new UdpConn(queue, packet.getSocketAddress(), socket));
        if(t != null) {
          queue.put(t);
        }
      }
    } catch (Exception e) {
      logger.error(Utils.stackTrace(e));
      logger.warn("Skip Upd packet.");
    } finally {
      if (socket != null) {
        socket.close();
      }
    }
  }
}
