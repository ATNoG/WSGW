package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskUnSub extends Task {
    private final String topic;
    private final WSConn wsconn;

    public TaskUnSub(String topic, WSConn wsconn) {
        super("unsub");
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
