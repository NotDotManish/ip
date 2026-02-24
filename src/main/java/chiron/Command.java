package chiron;

/**
 * Represents a generic command.
 * Commands are executed to perform actions in the application.
 */
public abstract class Command {
    /**
     * Executes the command.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return true if the application should exit, false otherwise.
     * @throws ChironException If an error occurs during execution.
     */
    public abstract boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException;
}
