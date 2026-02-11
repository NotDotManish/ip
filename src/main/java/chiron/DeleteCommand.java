package chiron;

/**
 * Represents a command to delete a task.
 */
public class DeleteCommand extends Command {
    private final String arg;

    /**
     * Constructs a DeleteCommand.
     *
     * @param arg The index of the task to delete (as a string).
     */
    public DeleteCommand(String arg) {
        this.arg = arg;
    }

    /**
     * Executes the delete command.
     * Removes the task at the specified index, saves to storage, and displays a
     * confirmation.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return False (continue running).
     * @throws ChironException If the index is invalid or saving fails.
     */
    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        int idx = Parser.parseIndex(arg);
        if (idx < 1 || idx > tasks.size()) {
            throw new ChironException("That task number doesn't exist.");
        }

        Task removed = tasks.remove(idx - 1);
        storage.save(tasks);

        ui.showDeleted(removed, tasks.size());
        return false;
    }
}
