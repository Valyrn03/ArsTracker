package application.characters;

import application.utils.CharacterFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CharacterBase {
    private String[] name;
    //Stores age, size, confidence (if applicable), decrepitude, warping, characteristics, fatigue (name -> digit)
    private HashMap<String, Integer> baseAttributes;
    //Ability <-> XP
    private HashMap<String, Integer> abilities;
    ArrayList<CharacterFeature> virtues;
    ArrayList<CharacterFeature> flaws;
    UUID id;

    public CharacterBase(String firstName, String lastName, int age){
        name = new String[]{firstName, lastName};
        baseAttributes = new HashMap<>();
        baseAttributes.put("Age", age);
        baseAttributes.put("Size", 0);
        setDefaultAttributes();
        abilities = new HashMap<>();
        id = UUID.randomUUID();
    }

    private void setDefaultAttributes(){
        baseAttributes.put("Intelligence", 0);
        baseAttributes.put("Perception", 0);
        baseAttributes.put("Strength", 0);
        baseAttributes.put("Stamina", 0);
        baseAttributes.put("Presence", 0);
        baseAttributes.put("Communication", 0);
        baseAttributes.put("Dexterity", 0);
        baseAttributes.put("Quickness", 0);
    }

    public void setAttribute(String attribute, int value){
        baseAttributes.put(attribute, value);
    }

    public String getName(){
        return name[0];
    }

    public String getSurname(){
        return name[1];
    }

    public int getAttribute(String attribute){
        if(baseAttributes.containsKey(attribute)){
            return baseAttributes.get(attribute);
        }else{
            return Integer.MAX_VALUE;
        }
    }
}
