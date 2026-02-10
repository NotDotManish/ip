package chiron;

public class MarkCommand extends Command {
    private final String arg;

    public MarkCommand(String arg) {
        this.arg = arg;
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        int idx = Parser.parseIndex(arg);
        if (idx < 1 || idx > tasks.size()) {
            throw new ChironException("That task number doesn't exist.");
        }

        tasks.setDone(idx - 1, true);
        storage.save(tasks);

        ui.showMarked(tasks.get(idx - 1), idx, true);
        return false;
    }
}
