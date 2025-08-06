package application.commands;

import application.characters.Character;
import org.beryx.textio.TextIO;

import java.util.ArrayList;

/*
    This command will build off of the editing command. The execute function will run through each requirement, rather than giving options and allowing players to select from those.

    It may also override a few of the other methods, but I am currently unsure
 */
public class CharacterCreator extends CharacterEditor{

    public CharacterCreator(TextIO source, ArrayList<Character> arr) {
        super(source, arr, null);
    }

    @Override
    public boolean execute(){
        return false;
    }
}
