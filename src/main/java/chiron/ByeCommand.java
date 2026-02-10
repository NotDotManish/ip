package chiron;

/**
 * Represents a command to exit the application.
 */
public class ByeCommand extends Command {
    @Override
    /**
     * Executes the exit command.
     * Displays the goodbye message and signals the application to terminate.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return True to signal exit.
     */
    public boolean execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
        return true;
    }
}
