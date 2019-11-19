package pt.ua.it.tnav.wsgw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.it.tnav.wsgw.logger.NullLogger;
import pt.ua.it.tnav.wsgw.task.Task;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Gateway class.
 * <p>
 * Rather simple pub/sub gateway that bridges udp and web-sockets.
 * Producers share data through a udp connection, it only supports JSON data.
 * Clients request data through a web-socket.
 * </p>
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Gateway {
  /**
   * Build, configures and deploys the gateway.
   * It supports graceful shutdown.
   *
   * @param args arguments from the console. Not used in this application.
   * @throws Exception to be deleted.
   */
  public static void main(String[] args) throws Exception {
    org.eclipse.jetty.util.log.Log.setLog(new NullLogger());
    final Logger logger = LoggerFactory.getLogger(Gateway.class);
    logger.info("WebSocket Gateway");

    Properties prop = new Properties();
    prop.load(new FileInputStream(args[0]));

    BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();

    Dispatcher pubSubManager = new Dispatcher(queue,
        Integer.parseInt(prop.getProperty("QueueSize")));
    WsEndpoint wsEndpoint = new WsEndpoint(queue,
        Integer.parseInt(prop.getProperty("WebSocketPort")));
    UdpEndpoint udpEndpoint = new UdpEndpoint(queue,
        new InetSocketAddress(new InetSocketAddress(0).getAddress(),
            Integer.parseInt(prop.getProperty("UDPPort"))));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      wsEndpoint.join();
      udpEndpoint.join();
      pubSubManager.join();
      logger.info("Gateway graceful shutdown.");
    }));

    pubSubManager.start();
    wsEndpoint.start();
    udpEndpoint.start();
  }
}
