package application.controllers;

import application.characters.CharacterBase;
import application.displays.LandingPage;
import application.utils.Abilities;
import application.utils.characterUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CharacterEditorController {
    @FXML
    private VBox extraneousTable;

    @FXML
    private VBox characteristicTable;

    @FXML
    private VBox abilityTable;

    @FXML
    private VBox featureTable;

    @FXML
    private VBox personalityTraits;

    @FXML
    private Button closeMenuButton;

    @FXML
    private Button saveAndCloseButton;

    private Stage currentStage;

    public void initialize(CharacterBase character, Stage currentStage){

        //Preparing Ability Table
        List<Abilities.Ability> abilities = character.getAbilities().keySet().stream().toList();
        abilities.sort(null);

        //Setting Formatter so that the text fields can only accept integers
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter());

        //Populating Ability Table
        for(Abilities.Ability ability : abilities){
            HBox item = new HBox();
            item.getChildren().add(new Label(ability.name()));
            TextField abilityField = new TextField(Integer.toString(character.getAbility(ability)));
            abilityField.setTextFormatter(formatter);
            abilityTable.getChildren().add(item);
        }

        //Populating Characteristics Table
        for(characterUtils.Attribute characteristic: characterUtils.Attribute.values()){
            HBox item = new HBox();
            item.getChildren().add(new Label(characteristic.name()));
            TextField characteristicField = new TextField(Integer.toString(character.getAttribute(characteristic)));
            characteristicField.setTextFormatter(formatter);
            characteristicTable.getChildren().add(item);
        }

        //
        closeMenuButton.setOnAction(event -> {
            try {
                CharacterSheetController.resetScene(character, currentStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void addNewAbility(CharacterBase character){
        Logger logger = Logger.getLogger(CharacterEditorController.class.getName());
        logger.info("NOT IMPLEMENTED");
    }
}
