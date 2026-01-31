import java.util.ArrayList;
import java.util.Scanner;

public class Chiron {

    private static final String LINE =
            "____________________________________________________________";

    private static final String MSG_GREETING_1 = "Hello! I'm Chiron!";
    private static final String MSG_GREETING_2 = "Wise enough to guide, young enough to call you out.";
    private static final String MSG_GREETING_3 = "What can I do for you?";

    private static final String MSG_BYE = "Chiron: Farewell. Train well—come back sharper.";

    private static final String MSG_UNKNOWN =
            "Chiron: I don't know that command yet.\n"
                    + "Try: todo <desc> | list | mark <n> | unmark <n> | bye";

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

        if (input.startsWith("todo")) {
            String desc = input.substring(4).trim();
            addTodo(desc);
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

        // Level 1 behavior: Echo unknown commands
        printEcho(input);
        return true;
    }

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

    private static void printEcho(String input) {
        printLine();
        System.out.println("Chiron: \"" + input + "\"");
        System.out.println("Chiron: Noted. Now what do you actually want to do?");
        printLine();
    }

    private static void addTodo(String desc) {
        if (desc.isEmpty()) {
            printLine();
            System.out.println("Chiron: A todo with no description? Brave.");
            System.out.println("Try: todo read CS2103T docs");
            printLine();
            return;
        }

        Task task = new Task(desc);
        tasks.add(task);

        printLine();
        System.out.println("Chiron: Added. Don’t abandon it.");
        System.out.println("  " + tasks.size() + ". " + task);
        System.out.println("Now you have " + tasks.size() + " task(s).");
        printLine();
    }

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
            System.out.println("  " + idx + ". " + task);
        } else {
            System.out.println("Chiron: Back to unfinished business.");
            System.out.println("  " + idx + ". " + task);
        }
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

    private static void printLine() {
        System.out.println(LINE);
    }

    private static class Task {
        private final String description;
        private boolean isDone;

        Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        void setDone(boolean done) {
            this.isDone = done;
        }

        @Override
        public String toString() {
            return "[T][" + (isDone ? "X" : " ") + "] " + description;
        }
    }
}
