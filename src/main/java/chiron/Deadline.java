package chiron;

import java.time.LocalDateTime;

/**
 * Represents a Deadline task.
 * A Deadline task has a description and a by-date/time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;
    private final boolean byHasTime;

    /**
     * Constructs a Deadline task.
     *
     * @param description The description of the task.
     * @param by          The deadline date/time.
     * @param byHasTime   True if the deadline includes a time component.
     */
    public Deadline(String description, LocalDateTime by, boolean byHasTime) {
        super(description);
        this.by = by;
        this.byHasTime = byHasTime;
    }

    /**
     * Returns the deadline date/time.
     *
     * @return The deadline LocalDateTime.
     */
    public LocalDateTime by() {
        return by;
    }

    /**
     * Checks if the deadline has a specific time component.
     *
     * @return True if time is specified, false otherwise.
     */
    public boolean byHasTime() {
        return byHasTime;
    }

    @Override
    public String typeIcon() {
        return "[D]";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + Parser.formatDateTime(by, byHasTime) + ")";
    }

    @Override
    public String toSaveString() {
        return "D | " + doneFlag() + " | " + description()
                + " | " + Parser.storeDateTime(by, byHasTime);
    }
}
