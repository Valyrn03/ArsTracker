package application;

import application.models.ArsCharacter;

import java.util.ArrayList;
import java.util.List;

public abstract class CharacterController implements Command {
    private CommandFramework framework;
    List<ArsCharacter> characters;

    public CharacterController(CommandFramework source){
        framework = source;
        characters = new ArrayList<>();
    }

    public int getInt(String prompt){
        return framework.getInt(prompt);
    }

    public static int calculateCost(int value){
        int absValue = Math.abs(value);
        return (absValue * (absValue + 1))/2;
    }

//    public int getOptions(List<String> options){
//        return framework.getOptions(options.stream());
//    }

    public String getString(String prompt){
        return framework.getString(prompt);
    }

    public boolean add(ArsCharacter character){
        if(characters.contains(character)){
            return false;
        }
        characters.add(character);
        return true;
    }
}
