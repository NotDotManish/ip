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

    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
        String input = rawInput.trim();

        if (input.equalsIgnoreCase("bye")) {
            printGoodbye();
            return false;
        }

        if (input.equalsIgnoreCase("list")) {
            printTaskList();
            return true;
        }

        if (input.startsWith("mark")) {
            String arg = input.substring(4).trim();
            markTask(arg, true);
            return true;
        }

        if (input.startsWith("unmark")) {
            String arg = input.substring(6).trim();
            markTask(arg, false);
            return true;
        }

        if (input.startsWith("delete")) {
            String arg = input.substring(6).trim();
            deleteTask(arg);
            return true;
        }

        if (input.startsWith("todo")) {
            String desc = input.substring(4).trim();
            addTodo(desc);
            return true;
        }

        if (input.startsWith("deadline")) {
            String rest = input.substring(8).trim();
            addDeadline(rest);
            return true;
        }

        if (input.startsWith("event")) {
            String rest = input.substring(5).trim();
            addEvent(rest);
            return true;
        }

        // Level 5: unknown commands handled gracefully
        printError("I don't know that command yet.", HELP);
        return true;
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
        if (desc.isEmpty()) {
            printError("A todo with no description? Brave.", "Try: todo read CS2103T spec");
            return;
        }

        Task task = new Todo(desc);
        tasks.add(task);

        printLine();
        System.out.println("Chiron: Added. Don’t abandon it.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static void addDeadline(String rest) {
        if (rest.isEmpty()) {
            printError("A deadline needs details.", "Try: deadline submit iP /by tonight");
            return;
        }

        int byPos = rest.indexOf("/by");
        if (byPos == -1) {
            printError("Deadlines need a /by.", "Try: deadline submit iP /by tonight");
            return;
        }

        String desc = rest.substring(0, byPos).trim();
        String by = rest.substring(byPos + 3).trim();

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

        printLine();
        System.out.println("Chiron: Deadline set. Don’t negotiate with time.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

    private static void addEvent(String rest) {
        if (rest.isEmpty()) {
            printError("An event needs details.", "Try: event meeting /from 2pm /to 4pm");
            return;
        }

        int fromPos = rest.indexOf("/from");
        int toPos = rest.indexOf("/to");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            printError("Events need both /from and /to.", "Try: event meeting /from 2pm /to 4pm");
            return;
        }

        String desc = rest.substring(0, fromPos).trim();
        String from = rest.substring(fromPos + 5, toPos).trim();
        String to = rest.substring(toPos + 3).trim();

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

        abstract String typeIcon();

        @Override
        public String toString() {
            return typeIcon() + "[" + statusIcon() + "] " + description;
        }
    }

    private static class Todo extends Task {
        Todo(String description) {
            super(description);
        }

        @Override
        String typeIcon() {
            return "[T]";
        }
    }

    private static class Deadline extends Task {
        private final String by;

        Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        String typeIcon() {
            return "[D]";
        }

        @Override
        public String toString() {
            return super.toString() + " (by: " + by + ")";
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
        String typeIcon() {
            return "[E]";
        }

        @Override
        public String toString() {
            return super.toString() + " (from: " + from + " to: " + to + ")";
        }
    }
}
