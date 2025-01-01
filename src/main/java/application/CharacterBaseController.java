package application;

import application.characters.CharacterBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CharacterBaseController {
    CharacterBase character;

    @FXML
    private Label characterName;
    @FXML
    private Button randomButton;
    @FXML
    private Label characterAge;

    @FXML
    protected void loadCharacter() {
        character = new CharacterBase("Sample", "Name", 20);
        characterName.setText(character.getName() + " " + character.getSurname());
        if(character.getAttribute("Age") != Integer.MAX_VALUE){
            characterAge.setText(Integer.toString(character.getAttribute("age")));
        }else{
            characterAge.setText("ERROR: Age Not Set");
        }
        randomButton.setDisable(true);
    }

    public void setCharacter(CharacterBase input){
        character = input;
    }
}