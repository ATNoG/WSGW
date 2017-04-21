package pt.ua.it.atnog.wsGw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ua.it.atnog.wsGw.logger.NullLogger;
import pt.ua.it.atnog.wsGw.task.Task;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Web Socket Pub/Sub gateway.
 * Producers share data through a UDP port, at this point it only supports JSON data.
 * Clients request data through a Web Socket.
 * It implements a simple Pub/Sub scheme.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class Gateway {
    public static void main(String[] args) throws Exception {
        org.eclipse.jetty.util.log.Log.setLog(new NullLogger());
        final Logger logger = LoggerFactory.getLogger(Gateway.class);
        logger.info("WebSocket Gateway");
        BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();

        Dispatcher pubSubManager = new Dispatcher(queue);
        WSEndpoint wsEndpoint = new WSEndpoint(queue, 8081);
        UDPEndpoint udpEndpoint = new UDPEndpoint(queue, new InetSocketAddress(InetAddress.getLocalHost(), 8888));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            udpEndpoint.join();
            pubSubManager.join();
            wsEndpoint.join();
            logger.info("Graceful shutdown.");
        }));

        pubSubManager.start();
        wsEndpoint.start();
        udpEndpoint.start();
    }
}
