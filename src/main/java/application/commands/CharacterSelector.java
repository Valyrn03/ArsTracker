package application.commands;

import application.characters.Character;
import application.terminal.CharacterController;
import application.terminal.Command;
import org.beryx.textio.TextIO;

import java.util.ArrayList;

import static application.displays.LandingPage.getCharacter;

public class CharacterSelector extends CharacterController {
    Character selectedCharacter;
    String characterName;


    public CharacterSelector(TextIO source, String name, ArrayList<Character> arr){
        super(source, arr);
        characterName = name;
        add(getCharacter("Elvira_Seoane"));
    }

    public CharacterSelector(TextIO source, ArrayList<Character> arr){
        super(source, arr);
    }

    @Override
    public boolean execute() {
        int index = 0;
        while(index < characters.size()){
            if(characters.get(index).getName().equals(characterName)){
                selectedCharacter = characters.get(index);
                return true;
            }else{
                index++;
            }
        }
        return false;
    }
}
