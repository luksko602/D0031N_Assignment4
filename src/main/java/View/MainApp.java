package View;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *Klass som startar GUI
 * @author Lukas Skog Andersen, luksok-8
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CalendarScene.fxml"));

        Scene scene = new Scene(root);
        //Styling av applikationen
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Kalendersynken");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *Startar appen
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }

}
