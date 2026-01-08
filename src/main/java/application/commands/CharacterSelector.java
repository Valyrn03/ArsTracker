package application.commands;

import application.ArsTrackerLauncher;
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
    String characterName;

    public CharacterSelector(CommandFramework framework, String name){
        super(framework);
        characterName = name;
    }

    public CharacterSelector(CommandFramework framework, ArrayList<Character> arr){
        super(framework);
    }

    @Override
    public boolean execute() {
        int index = 0;
        while(index < ArsTrackerLauncher.characters.size()){
            if(ArsTrackerLauncher.characters.get(index).getName().equals(characterName)){
                selectedCharacter = ArsTrackerLauncher.characters.get(index);
                return true;
            }else{
                index++;
            }
        }
        return false;
    }
}
