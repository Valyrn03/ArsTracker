package application.controllers;

import application.characters.Character;
import application.displays.CharacterSheetDisplay;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LandingPageController {
    @FXML
    private ListView<HBox> characterListView;

    public void setCharacters(ArrayList<Character> characters){
        Logger logger = Logger.getLogger("Landing Page Initialization");
        ArrayList<HBox> characterView = new ArrayList<>();
        for(Character character : characters){
            logger.log(Level.INFO, "Adding Character: " + character.getName());
            HBox view = new HBox();
            view.getChildren().add(new Label(character.toString()));

            view.getChildren().add(new Region());
            HBox.setHgrow(view.getChildren().get(1), Priority.ALWAYS);
            Button button = getCharacterViewButton(character, logger);
            view.getChildren().add(button);

            view.setSpacing(10);

            characterListView.getItems().add(view);
        }
        characterListView.getItems().forEach((child) -> {
            logger.log(Level.INFO, "Item: " + Arrays.toString(child.getChildren().toArray()));
        });
    }

    /*
    Creates the button that can be clicked that opens a new Character Sheet Stage
     */
    private Button getCharacterViewButton(Character character, Logger logger) {
        Button button = new Button("Show Character");
        button.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(CharacterSheetDisplay.class.getResource("characterView.fxml"));
            try {
                Parent root = (Parent) loader.load();
                CharacterSheetController controller = loader.getController();
                logger.info("[Landing Page] Set Character: " + character.toString());
                controller.setCharacter(character);
                Scene scene = new Scene(root, 1024, 760);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                logger.severe(String.format("[Landing Page] Exited on Loading Character 1$s", character));
                throw new RuntimeException(e);
            }
        });
        return button;
    }
}
