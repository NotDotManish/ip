package chiron;

/**
 * Handles user interface interactions.
 * Displays messages, errors, and task lists to the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    private static final String GREET_1 = "Chiron: I’m here.";
    private static final String GREET_2 = "Wise enough to guide. Young enough to grow with you.";
    private static final String GREET_3 = "Tell me - what are we working on today?";

    private static final String BYE = "Chiron: Rest well. Progress favors the consistent — not the rushed.";

    private static final String HELP = "Try:\n"
            + "  todo <desc>\n"
            + "  deadline <desc> /by <yyyy-mm-dd> [HHmm]\n"
            + "  event <desc> /from <yyyy-mm-dd> [HHmm] /to <yyyy-mm-dd> [HHmm]\n"
            + "  list\n"
            + "  mark <n>\n"
            + "  unmark <n>\n"
            + "  delete <n>\n"
            + "  bye";

    /**
     * Displays the welcome message to the user.
     */
    public void showGreeting() {
        line();
        System.out.println(GREET_1);
        System.out.println(GREET_2);
        System.out.println(GREET_3);
        line();
    }

    /**
     * Displays the exit message to the user.
     */
    public void showBye() {
        line();
        System.out.println(BYE);
        line();
    }

    /**
     * Displays the help message with available commands.
     */
    public void showHelp() {
        System.out.println(HELP);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        line();
        System.out.println("Chiron: " + message);
        line();
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks The TaskList containing tasks to display.
     */
    public void showList(TaskList tasks) {
        line();
        if (tasks.size() == 0) {
            System.out.println("Chiron: Your list is empty. Either you’re prepared - or you haven’t begun.");
        } else {
            System.out.println("Chiron: Here’s what you owe yourself:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        line();
    }

    /**
     * Displays a message confirming a task has been added.
     *
     * @param task    The task that was added.
     * @param size    The new total number of tasks.
     * @param message The confirmation message.
     */
    public void showAdded(Task task, int size, String message) {
        line();
        System.out.println("Chiron: " + message);
        System.out.println("  " + size + ". " + task);
        System.out.println("Now you have " + size + " task(s).");
        line();
    }

    /**
     * Displays a message confirming a task has been deleted.
     *
     * @param removed The task that was removed.
     * @param size    The new total number of tasks.
     */
    public void showDeleted(Task removed, int size) {
        line();
        System.out.println("Chiron: Letting go can be a form of clarity.");
        System.out.println("  " + removed);
        System.out.println("Now you have " + size + " task(s).");
        line();
    }

    /**
     * Displays a message confirming a task status change (mark/unmark).
     *
     * @param task   The task that was modified.
     * @param index  The index of the task.
     * @param isDone True if marked done, false if unmarked.
     */
    public void showMarked(Task task, int index, boolean isDone) {
        line();
        if (isDone) {
            System.out.println("Chiron: Well done. Momentum is built like this.");
        } else {
            System.out.println("Chiron: Then it isn’t finished yet. That’s alright.");
        }
        System.out.println("  " + index + ". " + task);
        line();
    }

    /**
     * Displays the results of a find operation.
     *
     * @param matches The list of tasks matching the keyword.
     */
    public void showFindResult(java.util.List<Task> matches) {
        line();
        if (matches.isEmpty()) {
            System.out.println("Chiron: I found nothing. Perhaps it never existed.");
        } else {
            System.out.println("Chiron: Here are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println((i + 1) + ". " + matches.get(i));
            }
        }
        line();
    }

    /**
     * Prints a separator line.
     */
    public void line() {
        System.out.println(LINE);
    }
}
