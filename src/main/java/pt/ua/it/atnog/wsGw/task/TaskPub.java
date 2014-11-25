package pt.ua.it.atnog.wsGw.task;

public class TaskPub extends Task {
    public final String data;

    public TaskPub(String data) {
        super("pub");
        this.data = data;
    }
}
