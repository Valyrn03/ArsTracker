package application;

import application.characters.CharacterBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Logger logger = Logger.getLogger("Character Loader");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("hello-view.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        CharacterBaseController controller = fxmlLoader.getController();
        logger.log(Level.INFO, fxmlLoader.toString());
        logger.log(Level.INFO, fxmlLoader.getController().toString());
        controller.setCharacter(new CharacterBase("Sample", "Character", 18));

        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Character Sheet");
        stage.setScene(scene);
        stage.show();
    }
}