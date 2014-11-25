package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskTopics extends Task {
    private WSConn conn;

    public TaskTopics(WSConn conn) {
        super("topics");
        this.conn = conn;
    }

    public WSConn conn() {
        return conn;
    }
}
