package application.terminal;

import application.characters.Character;
import org.beryx.textio.TextIO;

import java.util.ArrayList;

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
}
