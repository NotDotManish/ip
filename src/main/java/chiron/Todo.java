package chiron;

/**
 * Represents a Todo task.
 * A Todo task has only a description.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task.
     *
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String typeIcon() {
        return "[T]";
    }

    @Override
    public String toSaveString() {
        return "T | " + doneFlag() + " | " + description();
    }
}
