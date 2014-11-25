package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskSub extends Task {
    private WSConn conn;
    private String topic;

    public TaskSub(String topic, WSConn conn) {
        super("sub");
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
