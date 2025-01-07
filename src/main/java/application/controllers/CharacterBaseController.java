package application.controllers;

import application.characters.CharacterBase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.logging.Logger;

import static application.utils.characterUtils.abilityExperienceToScore;

public class CharacterBaseController {
    CharacterBase character;
    Logger logger;

    @FXML
    private Label characterName;
    @FXML
    private Label characterAge;
    @FXML
    private HBox basicStats;
    @FXML
    private HBox abilityTable;

    @FXML
    public void loadCharacter() {
        character = new CharacterBase("Sample", "Name", 20);
        characterName.setText(character.getName() + " " + character.getSurname());
        if(!Integer.valueOf((character.getAttribute("Age"))).equals(Integer.MAX_VALUE)){
            logger.info("Age is " + character.getAttribute("Age"));
            characterAge.setText(Integer.toString(character.getAttribute("Age")));
        }else{
            logger.info("Age N/A");
            characterAge.setText("ERROR: Age Not Set");
        }

        setCharacteristics();
        setAbilities();
    }

    private void setCharacteristics(){
        VBox statNames = new VBox(4);
        VBox statValues = new VBox(4);

        for(Map.Entry<String, Integer> entry: character.getAttributes().entrySet()){
            statNames.getChildren().add(new Label(entry.getKey()));
            statValues.getChildren().add(new Label(Integer.toString(entry.getValue())));
        }

        basicStats.getChildren().add(statNames);
        basicStats.getChildren().add(statValues);
    }

    private void setAbilities(){
        VBox abilities = new VBox(4);
        VBox specialities = new VBox(4);
        VBox scores = new VBox(4);

        for(Map.Entry entry : character.getAbilities().entrySet()){
            abilities.getChildren().add(new Label((String) entry.getKey()));
            scores.getChildren().add(new Label(Integer.toString(abilityExperienceToScore((Integer) entry.getValue()))));
            if(character.getSpeciality((String) entry.getKey()) == null){
                specialities.getChildren().add(new Label("None"));
            }else{
                specialities.getChildren().add(new Label(character.getSpeciality((String) entry.getKey())));
            }
        }

        abilityTable.getChildren().add(abilities);
        abilityTable.getChildren().add(scores);
        abilityTable.getChildren().add(specialities);
    }

    public void setCharacter(CharacterBase input){
        character = input;

        logger = Logger.getLogger(CharacterBaseController.class.getName());
    }
}