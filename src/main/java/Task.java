public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public boolean isDone() {
        return isDone;
    }

    protected String statusIcon() {
        return isDone ? "X" : " ";
    }

    protected String doneFlag() {
        return isDone ? "1" : "0";
    }

    public String description() {
        return description;
    }

    public abstract String typeIcon();

    public abstract String toSaveString();

    @Override
    public String toString() {
        return typeIcon() + "[" + statusIcon() + "] " + description;
    }
}
