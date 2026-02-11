package chiron;

import java.util.List;

/**
 * Represents a command to find tasks by keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand.
     *
     * @param keyword The keyword to search for.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword.trim();
    }

    /**
     * Executes the find command.
     * Searches for tasks containing the keyword and displays matching results.
     *
     * @param tasks   The list of tasks.
     * @param ui      The user interface.
     * @param storage The storage handler.
     * @return False (continue running).
     */
    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        if (keyword.isEmpty()) {
            throw new ChironException("Find what? Silence tells me nothing.");
        }

        List<Task> matches = tasks.find(keyword);
        ui.showFindResult(matches);
        return false;
    }
}
