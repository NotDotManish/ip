package chiron;

/**
 * Represents a command to add a Todo task.
 */
public class TodoCommand extends Command {
    private final String desc;

    /**
     * Constructs a TodoCommand.
     *
     * @param args The description of the todo task.
     */
    public TodoCommand(String args) {
        this.desc = args.trim();
    }

    @Override
    /**
     * Executes the todo command.
     * Creates a new Todo task, adds it to the list, saves to storage, and displays
     * a confirmation.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return False (continue running).
     * @throws ChironException If the description is empty or saving fails.
     */
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        if (desc.isEmpty()) {
            throw new ChironException("A todo with no description? Bold. Not helpful.");
        }

        Task t = new Todo(desc);
        tasks.add(t);
        storage.save(tasks);

        ui.showAdded(t, tasks.size(), "Noted. Small steps still move you forward.");
        return false;
    }
}
