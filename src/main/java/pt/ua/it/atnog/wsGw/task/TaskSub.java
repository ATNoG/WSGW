package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

/**
 * TaskSub class.
 * Task used when an entity subscribe data from the gateway.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskSub extends Task {
    private final WSConn wsconn;
    private final String topic;

    /**
     * TaskSub constructor.
     * Construct a TaskSub with a specific topic and wsconn.
     *
     * @param topic  Topic subscribed by an entity.
     * @param wsconn web-socket connection of the entity.
     */
    public TaskSub(String topic, WSConn wsconn) {
        super("sub");
        this.topic = topic;
        this.wsconn = wsconn;
    }

    /**
     * Returns the topic subscribed by and entity.
     *
     * @return the topic subscribed by and entity.
     */
    public String topic() {
        return topic;
    }

    /**
     * Returns the web-socket connection of an entity.
     *
     * @return the web-socket connection of an entity.
     */
    public WSConn wsconn() {
        return wsconn;
    }
}
