package application.terminal;

import application.characters.Character;
import application.characters.CharacterFeature;

public class CharacterCreator {
    private Character character;

    public CharacterCreator(){

    }

    public void initialize(String name, Character.CharacterType category){
        character = new Character(name, category);
    }

    public boolean addNewFeature(CharacterFeature feature){
        character.addFeature(feature);
        return true;
    }

    public Character close(){
        return character;
    }
}
