package application.terminal;

import application.characters.Character;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;

import static application.displays.LandingPage.getCharacter;

public class CommandController {
    ArrayList<Character> characters;
    Character selectedCharacter;

    public CommandController(ArrayList<Character> arr){
        characters = arr;
        characters.add(getCharacter("Elvira_Seoane"));
    }

    public String loadCharacters(){
        if(characters.isEmpty()){
            return "No Characters Found, use the \"create\" command to begin character creation";
        }
        StringBuilder builder = new StringBuilder("Characters:\n");
        for(Character character : characters){
            builder.append("\t" + character.getName() + "\n");
        }
        builder.append("Use the \"select\" command in order to choose a character");
        return builder.toString();
    }

    public String selectCharacter(String characterName){
        int index = 0;
        while(index < characters.size()){
            if(characters.get(index).getName().equals(characterName)){
                selectedCharacter = characters.get(index);
                return selectedCharacter.toString();
            }else{
                index++;
            }
        }
        return "No Character Found by that name, please try again";
    }

    public void createCharacter(TextTerminal terminal){

        
    }
}
