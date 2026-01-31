public class EventCommand extends Command {
    private final String args;

    public EventCommand(String args) {
        this.args = args.trim();
    }

    @Override
    public boolean execute(TaskList tasks, Ui ui, Storage storage) throws ChironException {
        if (args.isEmpty()) {
            throw new ChironException("An event needs details.");
        }

        int fromPos = args.indexOf("/from");
        int toPos = args.indexOf("/to");
        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new ChironException("Events need both /from and /to.");
        }

        String desc = args.substring(0, fromPos).trim();
        String fromRaw = args.substring(fromPos + 5, toPos).trim();
        String toRaw = args.substring(toPos + 3).trim();

        if (desc.isEmpty()) {
            throw new ChironException("Event description is missing.");
        }
        if (fromRaw.isEmpty()) {
            throw new ChironException("Event start date/time is missing.");
        }
        if (toRaw.isEmpty()) {
            throw new ChironException("Event end date/time is missing.");
        }

        Parser.ParsedDateTime from = Parser.parseDateTime(fromRaw);
        Parser.ParsedDateTime to = Parser.parseDateTime(toRaw);
        if (from == null || to == null) {
            throw new ChironException("I can only read dates as yyyy-mm-dd (optional time: HHmm).");
        }

        Task t = new Event(desc, from.value(), from.hasTime(), to.value(), to.hasTime());
        tasks.add(t);
        storage.save(tasks);

        ui.showAdded(t, tasks.size(), "Logged. Be present when the time comes.");
        return true;
    }
}
