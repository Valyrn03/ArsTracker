package application.commands;

import application.ArsTrackerLauncher;
import application.models.Character;
import application.terminal.CharacterController;
import application.terminal.CommandFramework;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelector extends CharacterController {
    Character selectedCharacter;
    String characterName;

    public CharacterSelector(CommandFramework framework, String name){
        super(framework);
        characterName = name;
    }

    public CharacterSelector(CommandFramework framework){
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
