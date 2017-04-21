package pt.ua.it.atnog.wsGw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.it.av.atnog.utils.Utils;
import pt.it.av.atnog.utils.json.JSONObject;
import pt.ua.it.atnog.wsGw.task.Task;
import pt.ua.it.atnog.wsGw.task.TaskPub;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;

/**
 *
 */
public class UDPEndpoint implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(UDPEndpoint.class);
    private final Thread t;
    private final BlockingQueue<Task> queue;
    private final SocketAddress address;
    private boolean done = true;

    public UDPEndpoint(BlockingQueue<Task> queue, SocketAddress address) {
        this.queue = queue;
        this.address = address;
        t = new Thread(this);
        done = false;
    }

    public void start() {
        t.start();
        logger.info("UDP Endpoint running.");
    }

    public void join() {
        try {
            logger.info("UDP Endpoint stopped.");
            DatagramSocket tmpSocket = new DatagramSocket();
            byte[] data = "{\"done\":true}".getBytes();
            DatagramPacket tmpPacket = new DatagramPacket(data, data.length, address);
            tmpSocket.send(tmpPacket);
            tmpSocket.close();
            t.join();
        } catch (Exception e) {
            logger.error(Utils.stackTrace(e));
        }
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(address);

            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            while (!done) {
                logger.info("UDP endpoint receiving.");
                socket.receive(packet);
                JSONObject json = JSONObject.read(new String(
                        packet.getData(), packet.getOffset(), packet.getLength()));
                logger.info("UDP packet received: " + json.toString());
                if (json.contains("done")) {
                    done = true;
                } else {
                    queue.put(new TaskPub(json));
                }
            }
        } catch (Exception e) {
            logger.error(Utils.stackTrace(e));
            done = true;
        } finally {
            if (socket != null)
                socket.close();
        }
    }
}
