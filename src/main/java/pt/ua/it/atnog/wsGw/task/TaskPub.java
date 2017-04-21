package pt.ua.it.atnog.wsGw.task;

import pt.it.av.atnog.utils.json.JSONObject;

public class TaskPub extends Task {
    private final JSONObject data;

    public TaskPub(JSONObject data) {
        super("pub");
        this.data = data;
    }

    public JSONObject data() {
        return data;
    }
}
