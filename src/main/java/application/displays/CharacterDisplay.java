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

public class CharacterDisplay extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Logger logger = Logger.getLogger("Character Loader");
        FXMLLoader fxmlLoader = new FXMLLoader(CharacterDisplay.class.getResource("characterView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        CharacterBaseController controller = fxmlLoader.getController();
        CharacterBase character = new CharacterBase("Sample", "Character", 18);
        character.improveAbility("Test Ability", 75);
        controller.setCharacter(character);
        controller.loadCharacter();

        Scene scene = new Scene(root, 1024, 760);
        stage.setTitle("Character Sheet");
        stage.setScene(scene);
        stage.show();
    }
}