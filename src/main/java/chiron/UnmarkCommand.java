package chiron;

/**
 * Represents a command to mark a task as not done.
 */
public class UnmarkCommand extends Command {
    private final String arg;

    /**
     * Constructs an UnmarkCommand.
     *
     * @param arg The index of the task to unmark (as a string).
     */
    public UnmarkCommand(String arg) {
        this.arg = arg;
    }

    @Override
    /**
     * Executes the unmark command.
     * Marks the task at the specified index as not done, saves to storage, and
     * displays a confirmation.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return False (continue running).
     * @throws ChironException If the index is invalid or saving fails.
     */
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        int idx = Parser.parseIndex(arg);
        if (idx < 1 || idx > tasks.size()) {
            throw new ChironException("That task number doesn't exist.");
        }

        tasks.setDone(idx - 1, false);
        storage.save(tasks);

        ui.showMarked(tasks.get(idx - 1), idx, false);
        return false;
    }
}
