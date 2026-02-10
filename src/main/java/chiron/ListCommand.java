package chiron;

/**
 * Represents a command to list all tasks.
 */
public class ListCommand extends Command {
    @Override
    /**
     * Executes the list command.
     * Displays the current list of tasks to the user.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return False (continue running).
     */
    public boolean execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks);
        return false;
    }
}
