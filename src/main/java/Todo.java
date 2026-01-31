public class Todo extends Task {
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
