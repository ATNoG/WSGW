package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskSub extends Task {
    private final WSConn wsconn;
    private final String topic;

    public TaskSub(String topic, WSConn wsconn) {
        super("sub");
        this.topic = topic;
        this.wsconn = wsconn;
    }

    public String topic() {
        return topic;
    }

    public WSConn wsconn() {
        return wsconn;
    }
}
