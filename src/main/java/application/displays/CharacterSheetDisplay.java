package application.displays;

import application.characters.CharacterBase;
import application.controllers.CharacterSheetController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Logger;

public class CharacterSheetDisplay {
    public static Scene start(CharacterBase character) throws IOException {
        Logger logger = Logger.getLogger("Character Loader");
        FXMLLoader fxmlLoader = new FXMLLoader(CharacterSheetDisplay.class.getResource("characterView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        CharacterSheetController controller = fxmlLoader.getController();
        controller.setCharacter(character);
        controller.loadCharacter();

        return new Scene(root, 1024, 760);
    }
}