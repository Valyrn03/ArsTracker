package application.controllers;

import application.characters.CharacterBase;
import application.displays.CharacterEditorDisplay;
import application.displays.CharacterSheetDisplay;
import application.displays.LandingPage;
import application.utils.Abilities;
import application.utils.CharacterFeature;
import application.utils.characterUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class CharacterSheetController {
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
    @FXML
    private Button editButton;

    /*
    Handles setting the basic data on the page, and the navigation buttons
     */
    public void loadCharacter() {
        logger.info("[Character Sheet] Loading Character " + character.toString());
        characterName.setText(character.getName() + " " + character.getSurname());
        if(!Integer.valueOf((character.getAttribute(characterUtils.ExtraneousAttribute.AGE))).equals(Integer.MAX_VALUE)){
            characterAge.setText(Integer.toString(character.getAttribute(characterUtils.ExtraneousAttribute.AGE)));
        }else{
            characterAge.setText("ERROR: Age Not Set");
        }

        logger.info("[Character Sheet]\tBeginning Attribute Setting");
        setCharacteristics();
        setAbilities();
        setFeatures();
        if(character.isMagus()){
            setArts();
        }

//        returnButton.setOnAction((event) -> {
//            Stage stage = (Stage) returnButton.getScene().getWindow();
//            stage.close();
//        });
//
        editButton.setOnAction(event -> {
            CharacterEditorDisplay.initialize(character, (Stage) refreshButton.getScene().getWindow());
        });
    }

    /*
    Sets Attributes Table
     */
    private void setCharacteristics(){
        logger.info("[Character Sheet] Loading Characteristics");
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

        basicStats.getChildren().add(statNames);
        basicStats.getChildren().add(statValues);
    }

    private void setAbilities(){
        logger.info("[Character Sheet] Loading Abilities");
        if(!abilityTable.getChildren().isEmpty()){
            abilityTable.getChildren().clear();
        }
        logger.info("Setting Abilities");
        VBox abilities = new VBox(4);
        VBox specialities = new VBox(4);
        VBox scores = new VBox(4);

        for(Abilities.Ability entry : character.getAbilities().keySet()){
            logger.info("Adding Ability: " + entry);
            abilities.getChildren().add(new Label(entry.name()));
            scores.getChildren().add(new Label(Integer.toString(character.getAbilityScore(entry))));
            if(character.getSpeciality(entry) == null){
                specialities.getChildren().add(new Label("None"));
            }else{
                specialities.getChildren().add(new Label(character.getSpeciality(entry)));
            }
        }

        if(!abilities.getChildren().isEmpty()){
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

    /*
    Sets the tables tracking Virtues and Flaws
     */
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

        logger = Logger.getLogger(CharacterSheetController.class.getName());
        logger.info("[Character Sheet] Loaded Character " + character.toString());

        loadCharacter();
    }

    /*
    Replaces and/or refreshes character display
     */
    public static void resetScene(CharacterBase character, Stage currentStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(CharacterSheetDisplay.class.getResource("characterView.fxml"));

        Parent root = (Parent) loader.load();
        CharacterSheetController controller = loader.getController();
        controller.setCharacter(character);

        Scene currentScene = new Scene(root, 1024, 760);

        currentStage.setScene(currentScene);
    }
}