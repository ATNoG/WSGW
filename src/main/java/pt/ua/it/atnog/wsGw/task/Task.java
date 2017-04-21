package pt.ua.it.atnog.wsGw.task;

/**
 *
 */
public class Task {
    private final String type;

    public Task(final String type) {
        this.type = type;
    }

    /**
     * @return
     */
    public String type() {
        return type;
    }
}
