package application.terminal;

import application.characters.Character;
import org.beryx.textio.TextTerminal;

public class CharacterCreator {
    private TextTerminal terminal;

    public CharacterCreator(){

    }

    public Character initialize(TextTerminal t){
        terminal = t;
        return null;
    }
}
