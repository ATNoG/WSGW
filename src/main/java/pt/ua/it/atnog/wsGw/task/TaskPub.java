package pt.ua.it.atnog.wsGw.task;

public class TaskPub extends Task {
    private String data;

    public TaskPub(String data) {
        super("pub");
        this.data = data;
    }

    public String data() {
        return data;
    }
}
