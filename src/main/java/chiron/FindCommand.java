package chiron;

import java.util.List;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword.trim();
    }

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
