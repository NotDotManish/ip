import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Chiron {

    private static final String LINE =
            "____________________________________________________________";

    private static final String MSG_GREETING_1 = "Hello! I'm Chiron!";
    private static final String MSG_GREETING_2 = "Wise enough to guide, young enough to call you out.";
    private static final String MSG_GREETING_3 = "What can I do for you?";

    private static final String MSG_BYE = "Chiron: Farewell. Train well—come back sharper.";

    private static final String HELP =
            "Try:\n"
                    + "  todo <desc>\n"
                    + "  deadline <desc> /by <time>\n"
                    + "  event <desc> /from <start> /to <end>\n"
                    + "  list\n"
                    + "  mark <n>\n"
                    + "  unmark <n>\n"
                    + "  delete <n>\n"
                    + "  bye";

    // ===== Level-7 storage paths =====
    private static final String DATA_DIR = "./data";
    private static final String DATA_PATH = "./data/chiron.txt";

    private static final ArrayList<Task> tasks = new ArrayList<>();

    // ===== Enums (A-Enums) =====

    private enum Command {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, UNKNOWN;

        static Command from(String word) {
            return switch (word.toLowerCase()) {
                case "bye" -> BYE;
                case "list" -> LIST;
                case "todo" -> TODO;
                case "deadline" -> DEADLINE;
                case "event" -> EVENT;
                case "mark" -> MARK;
                case "unmark" -> UNMARK;
                case "delete" -> DELETE;
                default -> UNKNOWN;
            };
        }
    }

    private enum TaskType {
        TODO("[T]"), DEADLINE("[D]"), EVENT("[E]");

        private final String icon;

        TaskType(String icon) {
            this.icon = icon;
        }

        String icon() {
            return icon;
        }
    }

    // ===== Main loop =====

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        loadTasks(); // Level-7: load saved tasks on startup
        printGreeting();

        boolean isRunning = true;
        while (isRunning) {
            String input = scanner.nextLine();
            isRunning = handleInput(input);
        }

        scanner.close();
    }

    /**
     * Returns true if the program should continue running, false if it should exit.
     */
    private static boolean handleInput(String rawInput) {
        String trimmed = rawInput.trim();

        if (trimmed.isEmpty()) {
            printError("Speak, or don’t. But don’t waste my time.", HELP);
            return true;
        }

        String[] parts = trimmed.split("\\s+", 2);
        Command cmd = Command.from(parts[0]);
        String args = (parts.length < 2) ? "" : parts[1];

        return switch (cmd) {
            case BYE -> {
                printGoodbye();
                yield false;
            }
            case LIST -> {
                printTaskList();
                yield true;
            }
            case MARK -> {
                markTask(args, true);
                yield true;
            }
            case UNMARK -> {
                markTask(args, false);
                yield true;
            }
            case DELETE -> {
                deleteTask(args);
                yield true;
            }
            case TODO -> {
                addTodo(args);
                yield true;
            }
            case DEADLINE -> {
                addDeadline(args);
                yield true;
            }
            case EVENT -> {
                addEvent(args);
                yield true;
            }
            case UNKNOWN -> {
                printError("I don't know that command yet.", HELP);
                yield true;
            }
        };
    }

    // ===== UI =====

    private static void printGreeting() {
        printLine();
        System.out.println(MSG_GREETING_1);
        System.out.println(MSG_GREETING_2);
        System.out.println(MSG_GREETING_3);
        printLine();
    }

    private static void printGoodbye() {
        printLine();
        System.out.println(MSG_BYE);
        printLine();
    }

    private static void printLine() {
        System.out.println(LINE);
    }

    private static void printError(String message, String hint) {
        printLine();
        System.out.println("Chiron: " + message);
        if (hint != null && !hint.isBlank()) {
            System.out.println(hint);
        }
        printLine();
    }

    // ===== Commands =====

    private static void printTaskList() {
        printLine();
        if (tasks.isEmpty()) {
            System.out.println("Chiron: Nothing on your list. Either you're free... or avoiding reality.");
        } else {
            System.out.println("Chiron: Here’s what you owe yourself:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        printLine();
    }

    private static void addTodo(String desc) {
        String cleaned = desc.trim();
        if (cleaned.isEmpty()) {
            printError("A todo with no description? Brave.", "Try: todo read CS2103T spec");
            return;
        }

        Task task = new Todo(cleaned);
        tasks.add(task);
        saveTasks(); // Level-7: save after modification

        printLine();
        System.out.println("Chiron: Added. Don’t abandon it.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static void addDeadline(String rest) {
        String cleaned = rest.trim();
        if (cleaned.isEmpty()) {
            printError("A deadline needs details.", "Try: deadline submit iP /by tonight");
            return;
        }

        int byPos = cleaned.indexOf("/by");
        if (byPos == -1) {
            printError("Deadlines need a /by.", "Try: deadline submit iP /by tonight");
            return;
        }

        String desc = cleaned.substring(0, byPos).trim();
        String by = cleaned.substring(byPos + 3).trim();

        if (desc.isEmpty()) {
            printError("Deadline description is missing.", "Try: deadline submit iP /by tonight");
            return;
        }
        if (by.isEmpty()) {
            printError("Deadline time is missing.", "Try: deadline submit iP /by tonight");
            return;
        }

        Task task = new Deadline(desc, by);
        tasks.add(task);
        saveTasks(); // Level-7

        printLine();
        System.out.println("Chiron: Deadline set. Don’t negotiate with time.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static void addEvent(String rest) {
        String cleaned = rest.trim();
        if (cleaned.isEmpty()) {
            printError("An event needs details.", "Try: event meeting /from 2pm /to 4pm");
            return;
        }

        int fromPos = cleaned.indexOf("/from");
        int toPos = cleaned.indexOf("/to");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            printError("Events need both /from and /to.", "Try: event meeting /from 2pm /to 4pm");
            return;
        }

        String desc = cleaned.substring(0, fromPos).trim();
        String from = cleaned.substring(fromPos + 5, toPos).trim();
        String to = cleaned.substring(toPos + 3).trim();

        if (desc.isEmpty()) {
            printError("Event description is missing.", "Try: event meeting /from 2pm /to 4pm");
            return;
        }
        if (from.isEmpty()) {
            printError("Event start time is missing.", "Try: event meeting /from 2pm /to 4pm");
            return;
        }
        if (to.isEmpty()) {
            printError("Event end time is missing.", "Try: event meeting /from 2pm /to 4pm");
            return;
        }

        Task task = new Event(desc, from, to);
        tasks.add(task);
        saveTasks(); // Level-7

        printLine();
        System.out.println("Chiron: Event logged. Show up.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static void markTask(String arg, boolean isDone) {
        int idx = parseIndex(arg);
        if (idx == -1) {
            printError("Give me a valid task number.", "Try: " + (isDone ? "mark 1" : "unmark 1"));
            return;
        }

        if (idx < 1 || idx > tasks.size()) {
            printError("That task number doesn't exist.", "Use 'list' to see valid task numbers.");
            return;
        }

        Task task = tasks.get(idx - 1);
        task.setDone(isDone);
        saveTasks(); // Level-7

        printLine();
        if (isDone) {
            System.out.println("Chiron: Good. Discipline looks good on you.");
        } else {
            System.out.println("Chiron: Back to unfinished business.");
        }
        System.out.println("  " + idx + ". " + task);
        printLine();
    }

    private static void deleteTask(String arg) {
        int idx = parseIndex(arg);
        if (idx == -1) {
            printError("Give me a valid task number to delete.", "Try: delete 1");
            return;
        }

        if (idx < 1 || idx > tasks.size()) {
            printError("That task number doesn't exist.", "Use 'list' to see valid task numbers.");
            return;
        }

        Task removed = tasks.remove(idx - 1);
        saveTasks(); // Level-7

        printLine();
        System.out.println("Chiron: Deleted. One less weight on your mind.");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static int parseIndex(String s) {
        String trimmed = s.trim();
        if (trimmed.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ===== Level-7 Storage =====

    private static void saveTasks() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdir(); // creates ./data if missing
            }

            try (FileWriter fw = new FileWriter(DATA_PATH)) {
                for (Task t : tasks) {
                    fw.write(t.toSaveString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            // Non-fatal: app still runs even if saving fails
            printError("Could not save tasks to disk.", null);
        }
    }

    private static void loadTasks() {
        File file = new File(DATA_PATH);

        if (!file.exists()) {
            // first run: nothing to load
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task t = parseSavedTask(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (FileNotFoundException e) {
            // Shouldn't happen if exists() is true, but keep safe
        }
    }

    private static Task parseSavedTask(String line) {
        // Format:
        // T | 1 | description
        // D | 0 | description | by
        // E | 0 | description | from | to
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null; // ignore corrupted line
        }

        String type = parts[0].trim();
        String doneFlag = parts[1].trim();
        String desc = parts[2].trim();

        Task t;

        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length < 4) return null;
                t = new Deadline(desc, parts[3].trim());
                break;
            case "E":
                if (parts.length < 5) return null;
                t = new Event(desc, parts[3].trim(), parts[4].trim());
                break;
            default:
                return null;
        }

        t.setDone("1".equals(doneFlag));
        return t;
    }

    // ===== Task model =====

    private static abstract class Task {
        private final String description;
        private boolean isDone;

        Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        void setDone(boolean done) {
            this.isDone = done;
        }

        String statusIcon() {
            return isDone ? "X" : " ";
        }

        String doneFlag() {
            return isDone ? "1" : "0";
        }

        String descriptionForSave() {
            return description;
        }

        abstract TaskType type();

        abstract String toSaveString();

        @Override
        public String toString() {
            return type().icon() + "[" + statusIcon() + "] " + description;
        }
    }

    private static class Todo extends Task {
        Todo(String description) {
            super(description);
        }

        @Override
        TaskType type() {
            return TaskType.TODO;
        }

        @Override
        String toSaveString() {
            return "T | " + doneFlag() + " | " + descriptionForSave();
        }
    }

    private static class Deadline extends Task {
        private final String by;

        Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        TaskType type() {
            return TaskType.DEADLINE;
        }

        @Override
        public String toString() {
            return super.toString() + " (by: " + by + ")";
        }

        @Override
        String toSaveString() {
            return "D | " + doneFlag() + " | " + descriptionForSave() + " | " + by;
        }
    }

    private static class Event extends Task {
        private final String from;
        private final String to;

        Event(String description, String from, String to) {
            super(description);
            this.from = from;
            this.to = to;
        }

        @Override
        TaskType type() {
            return TaskType.EVENT;
        }

        @Override
        public String toString() {
            return super.toString() + " (from: " + from + " to: " + to + ")";
        }

        @Override
        String toSaveString() {
            return "E | " + doneFlag() + " | " + descriptionForSave() + " | " + from + " | " + to;
        }
    }
}
