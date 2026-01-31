public class HelpCommand extends Command {
    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) {
        ui.line();
        ui.showHelp();
        ui.line();
        return true;
    }
}
