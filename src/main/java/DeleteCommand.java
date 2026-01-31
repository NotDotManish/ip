public class DeleteCommand extends Command {
    private final String arg;

    public DeleteCommand(String arg) {
        this.arg = arg;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        int idx = Parser.parseIndex(arg);
        if (idx < 1 || idx > tasks.size()) {
            throw new ChironException("That task number doesn't exist.");
        }

        Task removed = tasks.remove(idx - 1);
        storage.save(tasks);

        ui.showDeleted(removed, tasks.size());
        return true;
    }
}
