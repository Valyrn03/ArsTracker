package application.characters;

import application.utils.Abilities;
import application.utils.CharacterFeature;
import application.utils.characterUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

//Random comment

public class CharacterBase {
    private String[] name;
    //Stores age, size, confidence (if applicable), decrepitude, warping, characteristics, fatigue (name -> digit)
    private HashMap<characterUtils.Attribute, Integer> baseAttributes;
    //Ability <-> XP
    private HashMap<Abilities.Ability, Integer> abilities;
    private HashMap<Abilities.Ability, String> specialities;
    private HashMap<characterUtils.ExtraneousAttribute, Integer> attributes;
    //Tracking if not a grog
    private HashMap<String, Boolean> characterType;
    ArrayList<CharacterFeature> features;
    UUID id;
    Logger logger;

    public CharacterBase(String firstName, String lastName, int age, boolean magus){
        name = new String[]{firstName, lastName};
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(characterUtils.ExtraneousAttribute.AGE, age);
        attributes.put(characterUtils.ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new HashMap<>();
        specialities = new HashMap<>();
        id = UUID.randomUUID();
        characterType = new HashMap<>();
        characterType.put("Magus", false);
        characterType.put("Companion", false);
        if(magus){
            setMagus();
        }
        features = new ArrayList<>();
        logger = Logger.getLogger(Arrays.toString(this.name));
    }

    private void setDefaultAttributes(){
        for(characterUtils.Attribute attribute : characterUtils.Attribute.values()){
            if(logger == null){
                logger = Logger.getLogger(Arrays.toString(this.name));
                logger.info("Logger was null");
            }
            baseAttributes.put(attribute, 0);
        }
    }

    public void setAttribute(characterUtils.Attribute attribute, int value){
        baseAttributes.put(attribute, value);
        logger.info(String.format("Setting Attribute %s to value %d", attribute, value));
    }

    public String getName(){
        return name[0];
    }

    public String getSurname(){
        return name[1];
    }

    public int getAttribute(characterUtils.Attribute attribute){
        Logger logger = Logger.getLogger("Characters");
        logger.log(Level.FINER, baseAttributes.toString());
        return baseAttributes.getOrDefault(attribute, Integer.MAX_VALUE);
    }

    public int getAttribute(characterUtils.ExtraneousAttribute attribute){
        Logger logger = Logger.getLogger("Characters");
        logger.log(Level.FINER, baseAttributes.toString());
        return attributes.getOrDefault(attribute, Integer.MAX_VALUE);
    }

    public void improveAbility(String ability, int increment){
        int score = increment;
        if(abilities.containsKey(ability)){
            score += abilities.get(ability);
        }

        abilities.put(Abilities.Ability.valueOf(ability), score);
    }

    public void addSpeciality(Abilities.Ability ability, String speciality){
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

        for(characterUtils.Attribute attribute : characterUtils.Attribute.values()){
            int value = baseAttributes.get(String.valueOf(attribute));
            map.put(String.valueOf(attribute), value);
        }

        return map;
    }

    public HashMap<Abilities.Ability, Integer> getAbilities(){
        return abilities;
    }

    public HashMap<Abilities.Ability, String> getSpecialities(){
        return specialities;
    }

    public int getAbilityScore(Abilities.Ability ability){
        return characterUtils.abilityExperienceToScore(getAbility(ability));
    }

    public int getAbility(Abilities.Ability ability){
        return abilities.getOrDefault(ability, 0);
    }

    public ArrayList<CharacterFeature> getFeatures(){
        return features;
    }

    public String getSpeciality(Abilities.Ability ability){
        return specialities.get(ability);
    }
}
