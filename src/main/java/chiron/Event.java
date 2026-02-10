package chiron;

import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime from;
    private final boolean fromHasTime;
    private final LocalDateTime to;
    private final boolean toHasTime;

    public Event(String description,
                 LocalDateTime from, boolean fromHasTime,
                 LocalDateTime to, boolean toHasTime) {
        super(description);
        this.from = from;
        this.fromHasTime = fromHasTime;
        this.to = to;
        this.toHasTime = toHasTime;
    }

    public LocalDateTime from() {
        return from;
    }

    public boolean fromHasTime() {
        return fromHasTime;
    }

    public LocalDateTime to() {
        return to;
    }

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
