package application.terminal;

import application.characters.Character;
import org.beryx.textio.TextIO;

import java.util.ArrayList;
import java.util.List;

public abstract class CharacterController implements Command{
    public ArrayList<Character> characters;
    private CommandFramework framework;

    public CharacterController(CommandFramework source, ArrayList<Character> arr){
        framework = source;
        characters = arr;
    }

    public boolean add(Character character){
        characters.add(character);
        return true;
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
}
