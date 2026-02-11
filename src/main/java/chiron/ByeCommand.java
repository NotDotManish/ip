package chiron;

/**
 * Represents a command to exit the application.
 */
public class ByeCommand extends Command {
    /**
     * Executes the bye command.
     * Displays the exit message and triggers application termination.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return True (exit application).
     */
    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
        return true;
    }
}
