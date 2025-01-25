package application.displays;

import application.characters.CharacterBase;
import application.controllers.LandingPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LandingPage extends Application {
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        if(stage == null){
            stage = primaryStage;
        }
        stage.setTitle("Character Selector");
        stage.setScene(getScene());
        stage.show();
    }

    public static Scene getScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(LandingPage.class.getResource("landingPage.fxml"));

        ArrayList<CharacterBase> characters = new ArrayList<>();
        characters.add(new CharacterBase("Example", "Creation", 99, false));
        characters.get(0).improveAbility("Stealth", 16);

        Parent root = (Parent) loader.load();
        LandingPageController controller = loader.getController();
        controller.setCharacters(characters);

        return new Scene(root, 1024, 760);
    }

    public static void resetScene() throws IOException {
        stage.setScene(getScene());
    }
}
