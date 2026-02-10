package chiron;

/**
 * Represents a command to display help commands.
 */
public class HelpCommand extends Command {
    @Override
    /**
     * Executes the help command.
     * Displays the help message to the user.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return False (continue running).
     */
    public boolean execute(TaskList tasks, Ui ui, Storage storage) {
        ui.line();
        ui.showHelp();
        ui.line();
        return false;
    }
}
