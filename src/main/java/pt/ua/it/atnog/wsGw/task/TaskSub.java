package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskSub extends Task {
    public final WSConn conn;
    public final String topic;

    public TaskSub(String topic, WSConn conn) {
        super("sub");
        this.topic = topic;
        this.conn = conn;
    }
}
