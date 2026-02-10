package chiron;

public class DeadlineCommand extends Command {
    private final String args;

    public DeadlineCommand(String args) {
        this.args = args.trim();
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        if (args.isEmpty()) {
            throw new ChironException("A deadline needs details.");
        }

        int byPos = args.indexOf("/by");
        if (byPos == -1) {
            throw new ChironException("Deadlines need a /by. Precision matters here.");
        }

        String desc = args.substring(0, byPos).trim();
        String byRaw = args.substring(byPos + 3).trim();

        if (desc.isEmpty()) {
            throw new ChironException("Deadline description is missing.");
        }
        if (byRaw.isEmpty()) {
            throw new ChironException("Deadline date/time is missing.");
        }

        Parser.ParsedDateTime parsed = Parser.parseDateTime(byRaw);
        if (parsed == null) {
            throw new ChironException("I can only read dates as yyyy-mm-dd (optional time: HHmm).");
        }

        Task t = new Deadline(desc, parsed.value(), parsed.hasTime());
        tasks.add(t);
        storage.save(tasks);

        ui.showAdded(t, tasks.size(), "A deadline sharpens focus. Respect it.");
        return true;
    }
}
