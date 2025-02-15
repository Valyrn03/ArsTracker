package application.controllers;

import application.characters.CharacterBase;
import application.displays.CharacterSheetDisplay;
import javafx.fxml.FXML;
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

    public void setCharacters(ArrayList<CharacterBase> characters){
        Logger logger = Logger.getLogger("Landing Page Initialization");
        ArrayList<HBox> characterView = new ArrayList<>();
        for(CharacterBase character : characters){
            logger.log(Level.INFO, "Adding Character: " + character.getName() + " " + character.getSurname());
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

    private Button getCharacterViewButton(CharacterBase character, Logger logger) {
        Button button = new Button("Show Character");
        button.setOnAction(event -> {
            Scene scene = null;
            try {
                scene = CharacterSheetDisplay.start(character);
            } catch (IOException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }

            Stage stage = (Stage) characterListView.getScene().getWindow();
            if(scene != null){
                stage.setScene(scene);
            }
        });
        return button;
    }
}
