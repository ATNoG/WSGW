package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

public class TaskUnSubAll extends Task {
    public final WSConn conn;

    public TaskUnSubAll(WSConn conn) {
        super("unsuball");
        this.conn = conn;
    }
}
