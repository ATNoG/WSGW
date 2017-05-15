package pt.ua.it.atnog.wsGw.task;

import pt.it.av.atnog.utils.json.JSONObject;

/**
 * TaskPub class.
 * Task used when an entity publish data to the gateway.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class TaskPub extends Task {
    private final JSONObject data;

    /**
     * TaskPub constructor.
     * <p>
     * Constructs a TaskPub with the specified data.
     *
     * @param data data publish by an entity.
     */
    public TaskPub(JSONObject data) {
        super("pub");
        this.data = data;
    }

    /**
     * Returns the data associated with this task.
     *
     * @return the data associated with this task.
     */
    public JSONObject data() {
        return data;
    }
}
