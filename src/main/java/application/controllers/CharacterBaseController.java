package application.controllers;

import application.characters.CharacterBase;
import application.displays.CharacteristicDisplay;
import application.displays.LandingPage;
import application.utils.CharacterFeature;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
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
    private HBox artTable;
    @FXML
    private VBox virtues;
    @FXML
    private VBox flaws;
    @FXML
    private Button returnButton;
    @FXML
    private Button refreshButton;

    public void loadCharacter() {
        character = new CharacterBase("Sample", "Name", 20, false);
        characterName.setText(character.getName() + " " + character.getSurname());
        if(!Integer.valueOf((character.getAttribute("Age"))).equals(Integer.MAX_VALUE)){
            characterAge.setText(Integer.toString(character.getAttribute("Age")));
        }else{
            characterAge.setText("ERROR: Age Not Set");
        }

        setCharacteristics();
        setAbilities();
        setFeatures();
        if(character.isMagus()){
            setArts();
        }

        returnButton.setOnAction((event) -> {
            try {
                LandingPage.resetScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        refreshButton.setOnAction(event -> {
            refresh();
        });
    }

    private void setCharacteristics(){
        if(!basicStats.getChildren().isEmpty()){
            basicStats.getChildren().clear();
        }
        VBox statNames = new VBox(4);
        VBox statValues = new VBox(4);

        ArrayList<String> entries = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: character.getAttributes().entrySet()){
            statNames.getChildren().add(new Label(entry.getKey()));
            statValues.getChildren().add(new Label(Integer.toString(entry.getValue())));
            entries.add(String.format("\nAttribute: %s, Value: %s", entry.getKey(), entry.getValue()));
        }
        logger.info("Attributes Located:\n" + entries.toString());

        Button button = new Button("Edit Characteristics");
        button.setOnAction((event) -> {
            CharacteristicDisplay.initialize(character, false);
        });
        statNames.getChildren().add(button);

        basicStats.getChildren().add(statNames);
        basicStats.getChildren().add(statValues);
    }

    private void setAbilities(){
        if(!abilityTable.getChildren().isEmpty()){
            abilityTable.getChildren().clear();
        }
        logger.info("Setting Abilities");
        VBox abilities = new VBox(4);
        VBox specialities = new VBox(4);
        VBox scores = new VBox(4);

        for(Map.Entry entry : character.getAbilities().entrySet()){
            logger.info("Adding Ability: " + entry.toString());
            abilities.getChildren().add(new Label((String) entry.getKey()));
            scores.getChildren().add(new Label(Integer.toString(abilityExperienceToScore((Integer) entry.getValue()))));
            if(character.getSpeciality((String) entry.getKey()) == null){
                specialities.getChildren().add(new Label("None"));
            }else{
                specialities.getChildren().add(new Label(character.getSpeciality((String) entry.getKey())));
            }
        }

        if(abilities.getChildren().size() != 0){
            logger.info("Adding Abilities");
            abilityTable.getChildren().add(abilities);
            abilityTable.getChildren().add(scores);
            abilityTable.getChildren().add(specialities);
        }else{
            logger.info("No Abilities Set");
            abilityTable.getChildren().add(new Label("Abilities Not Set"));
        }
    }

    private void setArts() {
        artTable.getChildren().add(new Label("Not Implemented"));
    }

    private void setFeatures(){
        for(CharacterFeature feature : character.getFeatures()){
            if(feature.isVirtue()){
                virtues.getChildren().add(new Label(feature.toString()));
            }else{
                flaws.getChildren().add(new Label(feature.toString()));
            }
        }

        if(virtues.getChildren().size() == 1){
            virtues.getChildren().add(new Label("No Virtues Found"));
        }
        if(flaws.getChildren().size() == 1){
            flaws.getChildren().add(new Label("No Flaws Found"));
        }
    }

    public void setCharacter(CharacterBase input){
        character = input;

        logger = Logger.getLogger(CharacterBaseController.class.getName());
    }

    public void refresh(){
        setCharacteristics();
        setAbilities();
        setFeatures();
        if(character.isMagus()){
            setArts();
        }
    }
}