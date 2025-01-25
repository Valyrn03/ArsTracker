package application.displays;

import application.characters.CharacterBase;
import application.controllers.CharacteristicController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

public class CharacteristicDisplay {
    public static void initialize(CharacterBase character, Boolean editingAbility){
        Logger logger = Logger.getLogger("Characteristic Editor");

        try {
            URL url = CharacteristicDisplay.class.getResource("characteristicView.fxml");
            logger.info(url.toString());

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = (Parent) loader.load();
            CharacteristicController controller = loader.getController();
            controller.initialize(character, editingAbility);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 420, 315));
            stage.show();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }
}
