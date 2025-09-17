package application.terminal;

import application.characters.Character;
import org.beryx.textio.TextIO;

import java.util.ArrayList;
import java.util.List;

public abstract class CharacterController implements Command{
    private CommandFramework framework;
    List<Character> characters;

    public CharacterController(CommandFramework source){
        framework = source;
        characters = new ArrayList<>();
    }

    public int getInt(String prompt){
        return framework.getInt(prompt);
    }

    public void printToTerminal(String prompt){
        framework.source.getTextTerminal().println(prompt);
    }

    public static int calculateCost(int value){
        int absValue = Math.abs(value);
        return (absValue * (absValue + 1))/2;
    }

    public int getOptions(List<String> options){
        return framework.getOptions(options);
    }

    public String getString(String prompt){
        return framework.getString(prompt);
    }

    public boolean add(Character character){
        if(characters.contains(character)){
            return false;
        }
        characters.add(character);
        return true;
    }
}
