package chiron;

import java.util.ArrayList;
import java.util.Scanner;

public class Chiron {

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "chiron.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Chiron() {
        this.ui = new Ui();
        this.storage = new Storage(DATA_DIR, DATA_FILE);

        ArrayList<Task> loadedTasks;
        try {
            // storage.load() returns List<Task>, so convert to ArrayList<Task>
            loadedTasks = new ArrayList<>(storage.load());
        } catch (ChironException e) {
            ui.showError(e.getMessage());
            loadedTasks = new ArrayList<>();
        }

        this.tasks = new TaskList(loadedTasks);

        // A-Assertions: internal invariants (developer-only)
        assert ui != null : "ui should not be null";
        assert storage != null : "storage should not be null";
        assert tasks != null : "tasks should not be null";
    }

    public static void main(String[] args) {
        new Chiron().run();
    }

    public void run() {
        ui.showGreeting();

        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);

        while (!isExit) {
            String input = scanner.nextLine();
            assert input != null : "scanner returned null input line";

            try {
                Command command = Parser.parse(input);
                assert command != null : "Parser.parse returned null command";

                // Your project likely uses execute() returning boolean for exit
                isExit = command.execute(tasks, ui, storage);

            } catch (ChironException e) {
                ui.showError(e.getMessage());
            }
        }

        scanner.close();
    }
}
