package chiron;

import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;
    private final boolean byHasTime;

    public Deadline(String description, LocalDateTime by, boolean byHasTime) {
        super(description);
        this.by = by;
        this.byHasTime = byHasTime;
    }

    public LocalDateTime by() {
        return by;
    }

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
