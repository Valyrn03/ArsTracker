package application.characters;

import application.utils.CharacterFeature;
import application.utils.characterUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterBase {
    private String[] name;
    //Stores age, size, confidence (if applicable), decrepitude, warping, characteristics, fatigue (name -> digit)
    private HashMap<String, Integer> baseAttributes;
    //Ability <-> XP
    private HashMap<String, Integer> abilities;
    private HashMap<String, String> specialities;
    //Tracking if not a grog
    private HashMap<String, Boolean> characterType;
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
        specialities = new HashMap<>();
        id = UUID.randomUUID();
        characterType = new HashMap<>();
        characterType.put("Magus", false);
        characterType.put("Companion", false);
    }

    private void setDefaultAttributes(){
        for(String attribute : characterUtils.attributeList()){
            baseAttributes.put(attribute, 0);
        }
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
        Logger logger = Logger.getLogger("Characters");
        logger.log(Level.FINER, baseAttributes.toString());
        return baseAttributes.getOrDefault(attribute, Integer.MAX_VALUE);
    }

    public void improveAbility(String ability, int increment){
        int score = increment;
        if(abilities.containsKey(ability)){
            score += abilities.get(ability);
        }

        abilities.put(ability, increment);
    }

    public void addSpeciality(String ability, String speciality){
        specialities.put(ability, speciality);
    }

    @Override
    public String toString(){
        return name[0] + " " + name[1];
    }

    public void setMagus(){
        characterType.put("Magus", true);
    }

    public boolean isMagus(){
        return characterType.get("Magus");
    }

    public void setCompanion(){
        characterType.put("Companion", true);
    }

    public boolean isCompanion(){
        return characterType.get("Companion");
    }

    public HashMap<String, Integer> getAttributes(){
        HashMap<String, Integer> map = new HashMap<>();

        for(String attribute : characterUtils.attributeList()){
            map.put(attribute, baseAttributes.get(attribute));
        }

        return map;
    }

    public HashMap<String, Integer> getAbilities(){
        return abilities;
    }

    public HashMap<String, String> getSpecialities(){
        return specialities;
    }

    public int getAbilityScore(String ability){
        return abilities.get(ability);
    }

    public String getSpeciality(String ability){
        return specialities.get(ability);
    }
}
