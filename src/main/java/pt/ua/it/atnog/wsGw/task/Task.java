package pt.ua.it.atnog.wsGw.task;

public class Task {
    private final String type;

    Task(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
