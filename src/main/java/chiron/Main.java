package chiron;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Chiron using FXML.
 */
public class Main extends Application {

    private Chiron chiron = new Chiron("data/tasks.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Chiron");
            stage.setResizable(false);

            // Inject dependency
            fxmlLoader.<MainWindow>getController().setChiron(chiron);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
