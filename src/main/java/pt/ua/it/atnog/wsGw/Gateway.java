package pt.ua.it.atnog.wsGw;

import pt.ua.it.atnog.wsGw.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Gateway {
    public static void main(String[] args) {
        BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

        WSEndpoint wsEndpoint = new WSEndpoint(queue, 8081);
        Thread pubSubManager = new Thread(new PubSubManager(queue));
        Thread udpEndpoint = new Thread(new UDPEndpoint(queue, 1337));

        pubSubManager.start();
        wsEndpoint.start();
        udpEndpoint.start();
    }
}
