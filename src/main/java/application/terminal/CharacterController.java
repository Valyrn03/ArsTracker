package application.terminal;

import application.characters.Character;
import org.beryx.textio.TextIO;

import java.util.ArrayList;
import java.util.List;

public abstract class CharacterController extends Command{
    public ArrayList<Character> characters;

    public CharacterController(TextIO source, ArrayList<Character> arr){
        super(source);
        characters = arr;
    }

    public boolean add(Character character){
        characters.add(character);
        return true;
    }

    public int getInt(String prompt){
        return super.getInt(prompt);
    }

    public void print(String prompt){
        super.source.getTextTerminal().println(prompt);
    }

    public static int calculateCost(int value){
        int absValue = Math.abs(value);
        return (absValue * (absValue + 1))/2;
    }

    public int getOptions(List<String> options){
        return super.getOptions(options);
    }

    public String getString(String prompt){
        return super.getString(prompt);
    }
}
