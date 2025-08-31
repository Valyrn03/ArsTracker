package application.commands;

import application.characters.Character;
import application.terminal.CommandFramework;
import org.beryx.textio.TextIO;

import java.util.ArrayList;

/*
    This command will build off of the editing command. The execute function will run through each requirement, rather than giving options and allowing players to select from those.

    It may also override a few of the other methods, but I am currently unsure

    Character Table Structure:
        id
        name
        campaign_id
        birth_season
        character_type
        characteristics*
 */
public class CharacterCreator extends CharacterEditor{

    public CharacterCreator(CommandFramework framework, ArrayList<Character> arr) {
        super(framework, arr, null);
    }

    @Override
    public boolean execute(){
        return false;
    }

    public boolean addCharacter(){
        return false;
    }
}
