package application.displays;

import application.characters.Character;
import application.controllers.LandingPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class LandingPage extends Application {
    static Stage stage;
    static ArrayList<Character> characters;

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        if(stage == null){
            stage = primaryStage;
        }
        stage.setTitle("Character Selector");
        stage.setScene(getScene());
        stage.show();
    }

    public static Scene getScene() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(LandingPage.class.getResource("landingPage.fxml"));

        Parent root = (Parent) loader.load();
        LandingPageController controller = loader.getController();
        if(characters == null || characters.isEmpty()){
            characters = new ArrayList<>();
            characters.add(getCharacter("Elvira_Seoane"));
        }
        controller.setCharacters(characters);

        return new Scene(root, 1024, 760);
    }

    public static void resetScene() throws IOException, SQLException {
        stage.setScene(getScene());
    }

    private static Character getCharacter(String name){
        Logger logger = Logger.getLogger(LandingPage.class.getName());
        ArrayList<String> content;
        try{
            Path path = new File("src/main/resources/characters/" + name).toPath();
            content = new ArrayList<>(Files.readAllLines(path));
        } catch (NullPointerException exp){
            logger.info("Failed to Locate Character File\n\nException: " + exp.toString());
            return null;
        } catch (IOException exp){
            logger.info("Failed to Read Character File {" + name + "}\n\nException: " + exp.toString());
            return null;
        }

        logger.info("Located Character, Deserializing");

        return Character.
                deserialize(content);
    }
}
