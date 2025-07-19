package application.commands;

import application.characters.Character;
import application.terminal.CharacterController;
import org.beryx.textio.TextIO;

import java.util.ArrayList;

public class CharacterEditor extends CharacterController {

    public CharacterEditor(TextIO source, ArrayList<Character> arr) {
        super(source, arr);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
