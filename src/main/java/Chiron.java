import java.util.Scanner;

public class Chiron {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data", "chiron.txt");
        TaskList tasks;

        try {
            tasks = new TaskList(storage.load());
        } catch (ChironException e) {
            tasks = new TaskList();
            ui.showError(e.getMessage());
        }

        ui.showGreeting();

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            String input = scanner.nextLine();
            try {
                Command command = Parser.parse(input);
                isRunning = command.execute(tasks, ui, storage);
            } catch (ChironException e) {
                ui.showError(e.getMessage());
                ui.showHelp();
            }
        }
        scanner.close();
    }
}
