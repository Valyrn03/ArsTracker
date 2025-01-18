package application.displays;

import application.characters.CharacterBase;
import application.controllers.CharacterBaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class CharacterDisplay{
    public static Scene start(CharacterBase character) throws IOException {
        Logger logger = Logger.getLogger("Character Loader");
        FXMLLoader fxmlLoader = new FXMLLoader(CharacterDisplay.class.getResource("characterView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        CharacterBaseController controller = fxmlLoader.getController();
        controller.setCharacter(character);
        controller.loadCharacter();

        return new Scene(root, 1024, 760);
    }
}