package pt.ua.it.atnog.wsGw.task;

import pt.ua.it.atnog.wsGw.WSConn;

/**
 * TaskUnSub class.
 * Task used when an entity un-subscribe a specific topic.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskUnSub extends Task {
    private final String topic;
    private final WSConn wsconn;

    /**
     * TaskUnSub constructor.
     * Construct a TaskUnSub with a specific topic and wsconn.
     *
     * @param topic  topic un-subscribed by an entity.
     * @param wsconn web-socket connection of the entity.
     */
    public TaskUnSub(String topic, WSConn wsconn) {
        super("unsub");
        this.topic = topic;
        this.wsconn = wsconn;
    }

    /**
     * Returns the topic un-subscribe by and entity.
     *
     * @return the topic un-subscribe by and entity.
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
