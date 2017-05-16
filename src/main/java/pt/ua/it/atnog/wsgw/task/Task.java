package pt.ua.it.atnog.wsgw.task;

/**
 * Task class.
 * Represents an generic task for the gateway to perform.
 *
 * @author <a href="mailto:mariolpantunes@gmail.com">Mário Antunes</a>
 * @version 1.0
 */
public class Task {
  private final String type;

  /**
   * Task constructor.
   * Constructs a task with a specific type.
   *
   * @param type a string identifying the respective task.
   */
  public Task(final String type) {
    this.type = type;
  }

  /**
   * Returns a string identifying the respective task.
   *
   * @return a string identifying the respective task.
   */
  public String type() {
    return type;
  }

  @Override
  public String toString() {
    return "Task: " + type;
  }
}
