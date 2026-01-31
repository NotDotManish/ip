import java.util.ArrayList;
import java.util.Scanner;

public class Chiron {

    private static final String LINE =
            "____________________________________________________________";

    private static final String MSG_GREETING_1 = "Hello! I'm Chiron!";
    private static final String MSG_GREETING_2 = "Wise enough to guide, young enough to call you out.";
    private static final String MSG_GREETING_3 = "What can I do for you?";

    private static final String MSG_BYE = "Chiron: Farewell. Train well—come back sharper.";

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

        // Keep Level 1 “echo unknown” behaviour for now
        printEcho(input);
        return true;
    }

    // ===== Level 0 UI =====

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

    private static void printEcho(String input) {
        printLine();
        System.out.println("Chiron: \"" + input + "\"");
        System.out.println("Chiron: Noted. Now what do you actually want to do?");
        printLine();
    }

    // ===== Level 2/3/4 commands =====

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
            printLine();
            System.out.println("Chiron: A todo with no description? Brave.");
            System.out.println("Try: todo read CS2103T spec");
            printLine();
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
        // format: deadline <desc> /by <when>
        int byPos = rest.indexOf("/by");
        if (byPos == -1) {
            printLine();
            System.out.println("Chiron: Deadlines need a /by.");
            System.out.println("Try: deadline submit iP /by tonight");
            printLine();
            return;
        }

        String desc = rest.substring(0, byPos).trim();
        String by = rest.substring(byPos + 3).trim(); // after "/by"

        if (desc.isEmpty() || by.isEmpty()) {
            printLine();
            System.out.println("Chiron: Incomplete deadline. Give me both a description and a /by time.");
            System.out.println("Try: deadline return book /by Sunday");
            printLine();
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
        // format: event <desc> /from <start> /to <end>
        int fromPos = rest.indexOf("/from");
        int toPos = rest.indexOf("/to");

        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            printLine();
            System.out.println("Chiron: Events need both /from and /to.");
            System.out.println("Try: event project meeting /from 2pm /to 4pm");
            printLine();
            return;
        }

        String desc = rest.substring(0, fromPos).trim();
        String from = rest.substring(fromPos + 5, toPos).trim(); // between "/from" and "/to"
        String to = rest.substring(toPos + 3).trim();            // after "/to"

        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            printLine();
            System.out.println("Chiron: Incomplete event. Give me description, /from, and /to.");
            System.out.println("Try: event training /from 6pm /to 7pm");
            printLine();
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
            printLine();
            System.out.println("Chiron: Give me a task number. Example: " + (isDone ? "mark 1" : "unmark 1"));
            printLine();
            return;
        }
        if (idx < 1 || idx > tasks.size()) {
            printLine();
            System.out.println("Chiron: That task number doesn't exist. Use 'list' and pick a valid one.");
            printLine();
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

        boolean isDone() {
            return isDone;
        }

        String description() {
            return description;
        }

        abstract String typeIcon();

        String statusIcon() {
            return isDone ? "X" : " ";
        }

        @Override
        public String toString() {
            return typeIcon() + "[" + statusIcon() + "] " + description();
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
