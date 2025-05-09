package application.displays;

import application.characters.Character;
import application.controllers.CharacterEditorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class CharacterEditorDisplay {
    public static void initialize(Character character, Stage currentStage){
        Logger logger = Logger.getLogger("Characteristic Editor");

        try {
            URL url = CharacterEditorDisplay.class.getResource("characterEditor.fxml");
            logger.info(url.toString());

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = (Parent) loader.load();
            CharacterEditorController controller = loader.getController();
            controller.initialize(character, currentStage);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 420, 315));
            stage.show();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }
}
