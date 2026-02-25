package chiron;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {
    public static void main(String[] args) {
        try {
            Application.launch(Main.class, args);
        } catch (Throwable t) {
            System.out.println("GUI could not be started in this environment. Falling back to CLI...");
            new Chiron().run();
        }
    }
}
