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

    private StringBuilder buffer = new StringBuilder();

    /**
     * Retrieves the current response buffer and clears it.
     *
     * @return The buffered response string.
     */
    public String getResponse() {
        String response = buffer.toString();
        buffer.setLength(0);
        return response;
    }

    /**
     * Appends a message to the buffer and optionally prints it.
     *
     * @param message The message to append.
     */
    private void println(String message) {
        System.out.println(message);
        buffer.append(message).append("\n");
    }

    /**
     * Displays a message to the user, wrapped in horizontal lines.
     *
     * @param messages The messages to display.
     */
    public void showToUser(String... messages) {
        line();
        for (String msg : messages) {
            println(msg);
        }
        line();
    }

    /**
     * Displays the welcome message to the user.
     */
    public void showGreeting() {
        showToUser(GREET_1, GREET_2, GREET_3);
    }

    /**
     * Displays the exit message to the user.
     */
    public void showBye() {
        showToUser(BYE);
    }

    /**
     * Displays the help message with available commands.
     */
    public void showHelp() {
        showToUser(HELP);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showToUser("Chiron: " + message);
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks The TaskList containing tasks to display.
     */
    public void showList(TaskList tasks) {
        line();
        if (tasks.size() == 0) {
            println("Chiron: Your list is empty. Either you’re prepared - or you haven’t begun.");
        } else {
            printTasks(tasks.asUnmodifiableList(), "Chiron: Here’s what you owe yourself:");
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
        showToUser("Chiron: " + message, "  " + size + ". " + task, "Now you have " + size + " task(s).");
    }

    /**
     * Displays a message confirming a task has been deleted.
     *
     * @param removed The task that was removed.
     * @param size    The new total number of tasks.
     */
    public void showDeleted(Task removed, int size) {
        showToUser("Chiron: Letting go can be a form of clarity.", "  " + removed,
                "Now you have " + size + " task(s).");
    }

    /**
     * Displays a message confirming a task status change (mark/unmark).
     *
     * @param task   The task that was modified.
     * @param index  The index of the task.
     * @param isDone True if marked done, false if unmarked.
     */
    public void showMarked(Task task, int index, boolean isDone) {
        String msg = isDone ? "Chiron: Well done. Momentum is built like this."
                : "Chiron: Then it isn’t finished yet. That’s alright.";
        showToUser(msg, "  " + index + ". " + task);
    }

    /**
     * Displays the results of a find operation.
     *
     * @param matches The list of tasks matching the keyword.
     */
    public void showFindResult(java.util.List<Task> matches) {
        line();
        if (matches.isEmpty()) {
            println("Chiron: I found nothing. Perhaps it never existed.");
        } else {
            printTasks(matches, "Chiron: Here are the matching tasks in your list:");
        }
        line();
    }

    /**
     * Prints a separator line.
     */
    public void line() {
        println(LINE);
    }

    /**
     * Prints a list of tasks with a header.
     *
     * @param list   The list of tasks to print.
     * @param header The header message.
     */
    private void printTasks(java.util.List<Task> list, String header) {
        println(header);
        for (int i = 0; i < list.size(); i++) {
            println((i + 1) + ". " + list.get(i));
        }
    }
}
