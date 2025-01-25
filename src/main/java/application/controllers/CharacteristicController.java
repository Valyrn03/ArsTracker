package application.controllers;

import application.characters.CharacterBase;
import application.utils.characterUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CharacteristicController {
    @FXML
    private MenuButton characteristicSelector;

    @FXML
    private TextField valueOfCharacteristic;

    @FXML
    private Button closeMenuButton;

    @FXML
    private VBox selectorBox;

    public void initialize(CharacterBase character, Boolean editingAbility){
        ArrayList<MenuItem> items = new ArrayList<>();

        if(editingAbility){
            List<String> abilities = character.getAbilities().keySet().stream().toList();
            abilities.sort(null);

            for(String ability : abilities){
                MenuItem item = new MenuItem(String.valueOf(ability));
                item.setOnAction(event -> {
                    characteristicSelector.setText(String.valueOf(ability));
                });
                items.add(item);
            }

            MenuItem other = new MenuItem("Insert New");
            other.setOnAction(event -> {
                addNewAbility(character);
            });
        }else{
            for(characterUtils.AttributeList characteristic: characterUtils.AttributeList.values()){
                MenuItem item = new MenuItem(String.valueOf(characteristic));
                item.setOnAction(event -> {
                    characteristicSelector.setText(String.valueOf(characteristic));
                });
                items.add(item);
            }
        }
        characteristicSelector.getItems().addAll(items);

        closeMenuButton.setOnAction(event -> {
            if(!(characteristicSelector.getText().equals("Default") || characteristicSelector.getText().equals("Insert New")) || !valueOfCharacteristic.getText().equals("Current Value")){
                if(editingAbility){
                    character.improveAbility(characteristicSelector.getText(), Integer.parseInt(valueOfCharacteristic.getText()));
                }else{
                    character.setAttribute(characteristicSelector.getText(), Integer.parseInt(valueOfCharacteristic.getText()));
                }
            }else{
                Logger logger = Logger.getLogger(CharacteristicController.class.getName());
                logger.info("Attempting Selector: " + characteristicSelector.getText() + "\nValue: " + valueOfCharacteristic.getText());
            }
            Stage stage = (Stage) closeMenuButton.getScene().getWindow();
            stage.close();
        });
    }

    public void addNewAbility(CharacterBase character){
        Logger logger = Logger.getLogger(CharacteristicController.class.getName());
        logger.info("NOT IMPLEMENTED");
    }
}
