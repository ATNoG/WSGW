package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskUnSub extends Task {
    public final WSConn conn;
    public final String topic;

    public TaskUnSub(String topic, WSConn conn) {
        super("unsub");
        this.topic = topic;
        this.conn = conn;
    }
}
