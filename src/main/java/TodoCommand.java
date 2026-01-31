public class TodoCommand extends Command {
    private final String desc;

    public TodoCommand(String args) {
        this.desc = args.trim();
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        if (desc.isEmpty()) {
            throw new ChironException("A todo with no description? Bold. Not helpful.");
        }

        Task t = new Todo(desc);
        tasks.add(t);
        storage.save(tasks);

        ui.showAdded(t, tasks.size(), "Noted. Small steps still move you forward.");
        return true;
    }
}
