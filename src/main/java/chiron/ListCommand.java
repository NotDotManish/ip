package chiron;

/**
 * Represents a command to list all tasks.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command.
     * Displays all tasks in the list.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return False (continue running).
     */
    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showList(tasks);
        return false;
    }
}
