package application.controllers;

import application.characters.Ability;
import application.characters.Attribute;
import application.characters.Character;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
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

    public void initialize(Character character, Stage currentStage){

        //Preparing Ability Table
        List<Ability> abilities = character.getAbilities();
        abilities.sort(null);

        //Setting Formatter so that the text fields can only accept integers
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter());

        //Populating Ability Table
        for(Ability ability : abilities){
            HBox item = new HBox();
            item.getChildren().add(new Label(ability.getAbility()));
            TextField abilityField = new TextField(Integer.toString(ability.getExperience()));
            abilityField.setTextFormatter(formatter);
            abilityTable.getChildren().add(item);
        }

        //Populating Characteristics Table
        for(Attribute characteristic: Attribute.values()){
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

    public void addNewAbility(Character character){
        Logger logger = Logger.getLogger(CharacterEditorController.class.getName());
        logger.info("NOT IMPLEMENTED");
    }
}
