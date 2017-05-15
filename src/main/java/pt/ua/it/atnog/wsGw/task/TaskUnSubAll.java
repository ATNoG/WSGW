package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

/**
 * TaskUnSubAll class.
 * Task used when an entity un-subscribe all topics.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskUnSubAll extends Task {
    private final WSConn wsconn;

    /**
     * @param wsconn
     */
    public TaskUnSubAll(WSConn wsconn) {
        super("unsuball");
        this.wsconn = wsconn;
    }

    /**
     *
     * @return
     */
    public WSConn wsconn() {
        return wsconn;
    }
}
