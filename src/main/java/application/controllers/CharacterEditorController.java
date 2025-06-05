package application.controllers;

import application.characters.Character;
import application.utils.Abilities;
import application.utils.characterUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
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
        Logger logger = Logger.getLogger(CharacterEditorController.class.getName());

        //Preparing Ability Table
        ArrayList<Abilities.Ability> abilities = new ArrayList<>(character.getAbilities().keySet().stream().toList());
        abilities.sort(null);

        //Populating Ability Table
        for(Abilities.Ability ability : abilities){
            //Creating Field & Setting Formatter
            TextField abilityField = new TextField();
            abilityField.setTextFormatter(new TextFormatter<Integer>(new StringConverter<Integer>() {
                @Override
                public String toString(Integer integer) {
                    if(integer == null){
                        return "0";
                    }
                    return Integer.toString(integer);
                }

                @Override
                public Integer fromString(String s) {
                    if(s == null){
                        return 0;
                    }
                    return Integer.parseInt(s);
                }
            }));
            abilityField.setText(Integer.toString(character.getAbility(ability)));

            HBox item = new HBox();
            item.getChildren().add(new Label(ability.name()));
            item.getChildren().add(abilityField);

            abilityTable.getChildren().add(item);
        }

        //Populating Characteristics Table
        for(characterUtils.Attribute characteristic: characterUtils.Attribute.values()){
            //Creating Field, Setting Formatter, Setting Content
            TextField characteristicField = new TextField();
            characteristicField.setTextFormatter(new TextFormatter<>(new StringConverter<Integer>() {
                @Override
                public String toString(Integer integer) {
                    if(integer == null){
                        return "0";
                    }
                    return Integer.toString(integer);
                }

                @Override
                public Integer fromString(String s) {
                    if(s == null){
                        return 0;
                    }
                    return Integer.parseInt(s);
                }
            }));
            characteristicField.setText(Integer.toString(character.getAttribute(characteristic)));

            HBox item = new HBox();
            item.getChildren().add(new Label(characteristic.name()));
            item.getChildren().add(characteristicField);
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
