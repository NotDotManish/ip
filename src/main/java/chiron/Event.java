package chiron;

import java.time.LocalDateTime;

/**
 * Represents an Event task.
 * An Event task has a description, a start date/time, and an end date/time.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final boolean fromHasTime;
    private final LocalDateTime to;
    private final boolean toHasTime;

    /**
     * Constructs an Event task.
     *
     * @param description The description of the task.
     * @param from        The start date/time.
     * @param fromHasTime True if start time is included.
     * @param to          The end date/time.
     * @param toHasTime   True if end time is included.
     */
    public Event(String description,
            LocalDateTime from, boolean fromHasTime,
            LocalDateTime to, boolean toHasTime) {
        super(description);
        this.from = from;
        this.fromHasTime = fromHasTime;
        this.to = to;
        this.toHasTime = toHasTime;
    }

    /**
     * Returns the start date/time.
     *
     * @return The start LocalDateTime.
     */
    public LocalDateTime from() {
        return from;
    }

    /**
     * Checks if the start time is specified.
     *
     * @return True if start time is specified, false otherwise.
     */
    public boolean fromHasTime() {
        return fromHasTime;
    }

    /**
     * Returns the end date/time.
     *
     * @return The end LocalDateTime.
     */
    public LocalDateTime to() {
        return to;
    }

    /**
     * Checks if the end time is specified.
     *
     * @return True if end time is specified, false otherwise.
     */
    public boolean toHasTime() {
        return toHasTime;
    }

    @Override
    public String typeIcon() {
        return "[E]";
    }

    @Override
    public String toString() {
        return super.toString()
                + " (from: " + Parser.formatDateTime(from, fromHasTime)
                + " to: " + Parser.formatDateTime(to, toHasTime) + ")";
    }

    @Override
    public String toSaveString() {
        return "E | " + doneFlag() + " | " + description()
                + " | " + Parser.storeDateTime(from, fromHasTime)
                + " | " + Parser.storeDateTime(to, toHasTime);
    }
}
