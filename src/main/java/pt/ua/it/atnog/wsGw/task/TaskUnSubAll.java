package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskUnSubAll extends Task {
    private final WSConn wsconn;

    public TaskUnSubAll(WSConn wsconn) {
        super("unsuball");
        this.wsconn = wsconn;
    }

    public WSConn wsconn() {
        return wsconn;
    }
}
