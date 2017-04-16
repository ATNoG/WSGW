package pt.ua.it.atnog.wsGw;

import pt.ua.it.atnog.wsGw.task.Task;

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
    public static void main(String[] args) {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

        Dispatcher pubSubManager = new Dispatcher(queue);
        WSEndpoint wsEndpoint = new WSEndpoint(queue, 8081);
        UDPEndpoint udpEndpoint = new UDPEndpoint(queue, 8888);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            udpEndpoint.join();
            pubSubManager.join();
            wsEndpoint.join();
        }));

        pubSubManager.start();
        wsEndpoint.start();
        udpEndpoint.start();
    }
}
