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
                    + "Try: todo <desc> | list | bye";

    private static final ArrayList<String> tasks = new ArrayList<>();

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


     //Returns true if the program should continue running, false if it should exit.

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

        // Level 1: Echo everything else
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

        tasks.add(desc);

        printLine();
        System.out.println("Chiron: Added. Don’t abandon it.");
        System.out.println("  " + tasks.size() + ". [T] " + desc);
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
                System.out.println((i + 1) + ". [T] " + tasks.get(i));
            }
        }
        printLine();
    }

    private static void printUnknownCommand() {
        printLine();
        System.out.println(MSG_UNKNOWN);
        printLine();
    }

    private static void printLine() {
        System.out.println(LINE);
    }
}
