package application.commands;

import application.characters.Character;
import application.terminal.CharacterController;
import application.terminal.Command;
import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;

import java.util.ArrayList;
import java.util.List;

import static application.displays.LandingPage.getCharacter;

public class CharacterSelector extends CharacterController {
    Character selectedCharacter;
    List<Character> characters;
    String characterName;

    public CharacterSelector(CommandFramework framework, String name, ArrayList<Character> arr){
        super(framework);
        characterName = name;
        characters = arr;
        add(getCharacter("Elvira_Seoane"));
    }

    public CharacterSelector(CommandFramework framework, ArrayList<Character> arr){
        super(framework);
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
