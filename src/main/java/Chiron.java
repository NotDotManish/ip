import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.Scanner;

public class Chiron {

    private static final String LINE =
            "____________________________________________________________";

    // ===== Personality =====
    private static final String MSG_GREETING_1 = "Chiron: I’m here.";
    private static final String MSG_GREETING_2 = "Wise enough to guide. Young enough to grow with you.";
    private static final String MSG_GREETING_3 = "Tell me — what are we working on today?";

    private static final String MSG_BYE =
            "Chiron: Rest well. Progress favors the consistent — not the rushed.";

    private static final String HELP =
            "Try:\n"
                    + "  todo <desc>\n"
                    + "  deadline <desc> /by <yyyy-mm-dd> [HHmm]\n"
                    + "  event <desc> /from <yyyy-mm-dd> [HHmm] /to <yyyy-mm-dd> [HHmm]\n"
                    + "  list\n"
                    + "  mark <n>\n"
                    + "  unmark <n>\n"
                    + "  delete <n>\n"
                    + "  bye";

    // ===== Level-7 storage paths =====
    private static final String DATA_DIR = "./data";
    private static final String DATA_PATH = "./data/chiron.txt";

    // ===== Level-8 date formats =====
    // Accept:
    //   yyyy-MM-dd
    //   yyyy-MM-dd HHmm   (e.g. 2026-02-06 2359)
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    // Display:
    //   MMM dd yyyy
    //   MMM dd yyyy HHmm
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_DATE_TIME = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

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
                printError("That path doesn’t make sense yet.", HELP);
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
            System.out.println("Chiron: Your list is empty. Either you’re prepared — or you haven’t begun.");
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
            printError("A todo with no description? Bold. Not helpful.", "Try: todo read CS2103T spec");
            return;
        }

        Task task = new Todo(cleaned);
        tasks.add(task);
        saveTasks(); // Level-7: save after modification

        printLine();
        System.out.println("Chiron: Noted. Small steps still move you forward.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static void addDeadline(String rest) {
        String cleaned = rest.trim();
        if (cleaned.isEmpty()) {
            printError("A deadline needs details.", "Try: deadline submit iP /by 2026-02-05");
            return;
        }

        int byPos = cleaned.indexOf("/by");
        if (byPos == -1) {
            printError("Deadlines need a /by. Precision matters here.", "Try: deadline submit iP /by 2026-02-05");
            return;
        }

        String desc = cleaned.substring(0, byPos).trim();
        String byRaw = cleaned.substring(byPos + 3).trim();

        if (desc.isEmpty()) {
            printError("Deadline description is missing.", "Try: deadline submit iP /by 2026-02-05");
            return;
        }
        if (byRaw.isEmpty()) {
            printError("Deadline date/time is missing.", "Try: deadline submit iP /by 2026-02-05");
            return;
        }

        ParsedDateTime parsedBy = parseDateTime(byRaw);
        if (parsedBy == null) {
            printError("I can only read dates as yyyy-mm-dd (optional time: HHmm).",
                    "Examples:\n  deadline submit iP /by 2026-02-05\n  deadline submit iP /by 2026-02-05 2359");
            return;
        }

        Task task = new Deadline(desc, parsedBy.value, parsedBy.hasTime);
        tasks.add(task);
        saveTasks();

        printLine();
        System.out.println("Chiron: A deadline sharpens focus. Respect it.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static void addEvent(String rest) {
        String cleaned = rest.trim();
        if (cleaned.isEmpty()) {
            printError("An event needs details.", "Try: event meeting /from 2026-02-10 /to 2026-02-10");
            return;
        }

        int fromPos = cleaned.indexOf("/from");
        int toPos = cleaned.indexOf("/to");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            printError("Events need both /from and /to.", "Try: event meeting /from 2026-02-10 /to 2026-02-10");
            return;
        }

        String desc = cleaned.substring(0, fromPos).trim();
        String fromRaw = cleaned.substring(fromPos + 5, toPos).trim();
        String toRaw = cleaned.substring(toPos + 3).trim();

        if (desc.isEmpty()) {
            printError("Event description is missing.", "Try: event meeting /from 2026-02-10 /to 2026-02-10");
            return;
        }
        if (fromRaw.isEmpty()) {
            printError("Event start date/time is missing.", "Try: event meeting /from 2026-02-10 /to 2026-02-10");
            return;
        }
        if (toRaw.isEmpty()) {
            printError("Event end date/time is missing.", "Try: event meeting /from 2026-02-10 /to 2026-02-10");
            return;
        }

        ParsedDateTime parsedFrom = parseDateTime(fromRaw);
        ParsedDateTime parsedTo = parseDateTime(toRaw);

        if (parsedFrom == null || parsedTo == null) {
            printError("I can only read dates as yyyy-mm-dd (optional time: HHmm).",
                    "Examples:\n  event meeting /from 2026-02-10 /to 2026-02-12\n"
                            + "  event meeting /from 2026-02-10 1400 /to 2026-02-10 1600");
            return;
        }

        Task task = new Event(desc, parsedFrom.value, parsedFrom.hasTime, parsedTo.value, parsedTo.hasTime);
        tasks.add(task);
        saveTasks();

        printLine();
        System.out.println("Chiron: Logged. Be present when the time comes.");
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
        saveTasks();

        printLine();
        if (isDone) {
            System.out.println("Chiron: Well done. Momentum is built like this.");
        } else {
            System.out.println("Chiron: Then it isn’t finished yet. That’s alright.");
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
        saveTasks();

        printLine();
        System.out.println("Chiron: Letting go can be a form of clarity.");
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

    // ===== Level-8 Date/Time parsing =====

    private static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }
    }

    private static ParsedDateTime parseDateTime(String raw) {
        String s = raw.trim();
        if (s.isEmpty()) {
            return null;
        }

        // date + time (contains a space)
        if (s.contains(" ")) {
            try {
                LocalDateTime dt = LocalDateTime.parse(s, INPUT_DATE_TIME);
                return new ParsedDateTime(dt, true);
            } catch (DateTimeParseException ignored) {
                return null;
            }
        }

        // date-only
        try {
            LocalDate d = LocalDate.parse(s, INPUT_DATE);
            return new ParsedDateTime(LocalDateTime.of(d, LocalTime.MIDNIGHT), false);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private static String formatDateTime(LocalDateTime dt, boolean hasTime) {
        return hasTime ? dt.format(OUTPUT_DATE_TIME) : dt.toLocalDate().format(OUTPUT_DATE);
    }

    private static String storeDateTime(LocalDateTime dt, boolean hasTime) {
        // store back in an input-compatible format
        return hasTime ? dt.format(INPUT_DATE_TIME) : dt.toLocalDate().format(INPUT_DATE);
    }

    // ===== Level-7 Storage =====

    private static void saveTasks() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try (FileWriter fw = new FileWriter(DATA_PATH)) {
                for (Task t : tasks) {
                    fw.write(t.toSaveString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            printError("I couldn’t write your tasks to disk. We’ll keep going — but fix this soon.", null);
        }
    }

    private static void loadTasks() {
        File file = new File(DATA_PATH);

        if (!file.exists()) {
            // First run: nothing to load
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
            // Ignore: start empty
        }
    }

    private static Task parseSavedTask(String line) {
        // Format:
        // T | 1 | description
        // D | 0 | description | yyyy-MM-dd [HHmm]
        // E | 0 | description | yyyy-MM-dd [HHmm] | yyyy-MM-dd [HHmm]
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
                ParsedDateTime byParsed = parseDateTime(parts[3].trim());
                if (byParsed == null) return null; // old invalid line: skip
                t = new Deadline(desc, byParsed.value, byParsed.hasTime);
                break;
            case "E":
                if (parts.length < 5) return null;
                ParsedDateTime fromParsed = parseDateTime(parts[3].trim());
                ParsedDateTime toParsed = parseDateTime(parts[4].trim());
                if (fromParsed == null || toParsed == null) return null; // old invalid line: skip
                t = new Event(desc, fromParsed.value, fromParsed.hasTime, toParsed.value, toParsed.hasTime);
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
        private final LocalDateTime by;
        private final boolean byHasTime;

        Deadline(String description, LocalDateTime by, boolean byHasTime) {
            super(description);
            this.by = by;
            this.byHasTime = byHasTime;
        }

        @Override
        TaskType type() {
            return TaskType.DEADLINE;
        }

        @Override
        public String toString() {
            return super.toString() + " (by: " + formatDateTime(by, byHasTime) + ")";
        }

        @Override
        String toSaveString() {
            return "D | " + doneFlag() + " | " + descriptionForSave() + " | " + storeDateTime(by, byHasTime);
        }
    }

    private static class Event extends Task {
        private final LocalDateTime from;
        private final boolean fromHasTime;
        private final LocalDateTime to;
        private final boolean toHasTime;

        Event(String description, LocalDateTime from, boolean fromHasTime,
              LocalDateTime to, boolean toHasTime) {
            super(description);
            this.from = from;
            this.fromHasTime = fromHasTime;
            this.to = to;
            this.toHasTime = toHasTime;
        }

        @Override
        TaskType type() {
            return TaskType.EVENT;
        }

        @Override
        public String toString() {
            return super.toString()
                    + " (from: " + formatDateTime(from, fromHasTime)
                    + " to: " + formatDateTime(to, toHasTime) + ")";
        }

        @Override
        String toSaveString() {
            return "E | " + doneFlag() + " | " + descriptionForSave()
                    + " | " + storeDateTime(from, fromHasTime)
                    + " | " + storeDateTime(to, toHasTime);
        }
    }
}
