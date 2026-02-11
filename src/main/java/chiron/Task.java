package chiron;

/**
 * Represents a generic task.
 * A task has a description and a completion status.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Constructs a task with the given description.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Sets the completion status of the task.
     *
     * @param done True if the task is done, false otherwise.
     */
    public void setDone(boolean done) {
        this.isDone = done;
    }

    /**
     * Checks if the task is done.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon for the task.
     * "X" if done, " " if not done.
     *
     * @return The status icon string.
     */
    protected String statusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns a string flag representing the completion status for storage ("1" for
     * done, "0" for not done).
     *
     * @return "1" if done, "0" otherwise.
     */
    protected String doneFlag() {
        return isDone ? "1" : "0";
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the type icon of the task (e.g., "T", "D", "E").
     *
     * @return The type icon string.
     */
    public abstract String typeIcon();

    /**
     * Returns the string representation of the task for storage.
     *
     * @return The storage-friendly string.
     */
    public abstract String toSaveString();

    @Override
    public String toString() {
        return typeIcon() + "[" + statusIcon() + "] " + description;
    }
}
