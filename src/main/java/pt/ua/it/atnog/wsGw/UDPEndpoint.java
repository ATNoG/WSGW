package pt.ua.it.atnog.wsGw;

import pt.ua.it.atnog.wsGw.task.Task;
import pt.ua.it.atnog.wsGw.task.TaskPub;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;

public class UDPEndpoint implements Runnable {
    private BlockingQueue<Task> queue;
    private int port;

    public UDPEndpoint(BlockingQueue<Task> queue, int port) {
        this.queue = queue;
        this.port = port;

    }

    public void run() {
        boolean done = false;
        DatagramSocket server = null;

        try {
            server = new DatagramSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            done = true;
        }

        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (!done) {
            try {
                server.receive(packet);
                queue.put(new TaskPub(new String(packet.getData(), packet.getOffset(), packet.getLength())));
            } catch (Exception e) {
                e.printStackTrace();
                done = true;
            }
        }
        server.close();
    }
}
