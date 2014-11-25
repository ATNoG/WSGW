package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskUnSub extends Task {
    private WSConn conn;
    private String topic;

    public TaskUnSub(String topic, WSConn conn) {
        super("unsub");
        this.topic = topic;
        this.conn = conn;
    }

    public WSConn conn() {
        return conn;
    }

    public String topic() {
        return topic;
    }
}
