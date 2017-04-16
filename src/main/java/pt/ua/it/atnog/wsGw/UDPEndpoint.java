package pt.ua.it.atnog.wsGw;

import pt.ua.it.atnog.wsGw.task.Task;
import pt.ua.it.atnog.wsGw.task.TaskPub;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

public class UDPEndpoint implements Runnable {
    private final Thread t;
    private final BlockingQueue<Task> queue;
    private final int port;
    private boolean done = true;

    public UDPEndpoint(BlockingQueue<Task> queue, int port) {
        this.queue = queue;
        this.port = port;
        done = false;
        t = new Thread(this);

    }

    public void start() {
        t.start();
    }

    public void join() {
        try {
            done = true;
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(3000);

            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            while (!done) {
                socket.receive(packet);
                queue.put(new TaskPub(new String(packet.getData(), packet.getOffset(), packet.getLength())));
            }
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            e.printStackTrace();
            done = true;
        } finally {
            if (socket != null)
                socket.close();
        }
    }
}
