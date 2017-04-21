package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskTopics extends Task {
    private final WSConn wsconn;

    public TaskTopics(WSConn wsconn) {
        super("topics");
        this.wsconn = wsconn;
    }

    public WSConn wsconn() {
        return wsconn;
    }
}
