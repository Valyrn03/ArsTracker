package application.characters;

import application.utils.Abilities;
import application.utils.CharacterFeature;
import application.utils.characterUtils;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.sort;

//Random comment

public class Character implements Serializable, Comparable<Character> {
    private String name;
    //Stores age, size, confidence (if applicable), decrepitude, warping, characteristics, fatigue (name -> digit)
    private HashMap<characterUtils.Attribute, Integer> baseAttributes;
    //Ability <-> XP
    private HashMap<Abilities.Ability, Integer> abilities;
    private HashMap<characterUtils.ExtraneousAttribute, Integer> attributes;
    //Tracking if not a grog
    private CharacterType characterType;
    ArrayList<CharacterFeature> features;
    UUID id;
    Logger logger;

    public Character(String name, int age, String characterCategory){
        this.name = name;
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(characterUtils.ExtraneousAttribute.AGE, age);
        attributes.put(characterUtils.ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new HashMap<>();
        id = UUID.randomUUID();
        characterType = CharacterType.valueOf(characterCategory.toUpperCase());
        features = new ArrayList<>();
        logger = Logger.getLogger(this.name);
    }

    public static enum CharacterType {
        MAGUS,
        COMPANION,
        GROG
    }

    private void setDefaultAttributes(){
        for(characterUtils.Attribute attribute : characterUtils.Attribute.values()){
            if(logger == null){
                logger = Logger.getLogger(this.name);
                logger.info("Logger was null");
            }
            baseAttributes.put(attribute, 0);
        }
    }

    public void setAttributes(ArrayList<Integer> characteristics){
        characterUtils.Attribute[] attributes = characterUtils.Attribute.values();
        for(int i = 0; i < characteristics.size(); i++){
            baseAttributes.put(attributes[i], characteristics.get(i));
        }
    }

    public String getName(){
        return name;
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

    public void improveAbility(Abilities.Ability ability, int increment){
        int score = increment;
        if(abilities.containsKey(ability)){
            score += abilities.get(ability);
        }

        abilities.put(ability, score);
    }

    @Override
    public String toString(){
        return getName();
    }

    public HashMap<String, Integer> getAttributes(){
        HashMap<String, Integer> map = new HashMap<>();

        for(characterUtils.Attribute attribute : characterUtils.Attribute.values()){
            int value = baseAttributes.get(attribute);
            map.put(String.valueOf(attribute), value);
        }

        return map;
    }

    public HashMap<Abilities.Ability, Integer> getAbilities(){
        return abilities;
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

    public void addFeature(String feature, boolean isVirtue){
        features.add(new CharacterFeature(feature, "", isVirtue));
    }

    public CharacterType getType(){
        return characterType;
    }

    public String serialize(){
        StringBuilder builder = new StringBuilder();
        builder.append(getName()).append("\n");
        builder.append(getAttribute(characterUtils.ExtraneousAttribute.AGE)).append(" ").append(getType()).append("\n");

        ArrayList<CharacterFeature> flaws = new ArrayList<>();

        ArrayList<CharacterFeature> features = getFeatures();
        sort(features);

        builder.append("Virtues").append("\n");
        for(CharacterFeature feature : features){
            if(feature.isVirtue()){
                builder.append("\t").append(feature.toString()).append("\n");
            }else{
                flaws.add(feature);
            }
        }

        sort(flaws);
        builder.append("Flaws").append("\n");
        for(CharacterFeature feature : flaws){
            builder.append("\t").append(feature.toString()).append("\n");
        }

        builder.append("Abilities").append("\n");
        ArrayList<Abilities.Ability> abilities = new ArrayList<>(getAbilities().keySet().stream().toList());
        sort(abilities);
        for(Abilities.Ability ability : abilities){
            builder.append("\t").append(getAbility(ability)).append(" ").append(ability.name()).append("\n");
        }

        return builder.toString();
    }

    public Logger exportLogger(){
        return logger;
    }

    /*
    Input: ArrayList of Lines
    Structure:
        Name
        Characteristics
        Virtues
            *
        Flaws
            *
        Abilities
            *
     */
    public static Character deserialize(ArrayList<String> content){
        //CREATING CHARACTER
        String name = content.getFirst();
        String[] ageAndType = content.get(1).split(" ");
        Character character = new Character(name, Integer.parseInt(ageAndType[0]), ageAndType[1]);

        Logger logger = character.exportLogger();

        //HANDLING CHARACTERISTICS
        String[] characteristicStrings = content.get(2).split(" ");
        logger.info("[Deserialization] Loaded Characteristics: " + Arrays.toString(characteristicStrings));
        ArrayList<Integer> characteristics = new ArrayList<>();
        for(int i = 1; i < characteristicStrings.length; i += 2){
            characteristics.add(Integer.parseInt(characteristicStrings[i]));
        }
        character.setAttributes(characteristics);

        //Virtues & Flaws
        int counter = 4;
        while(!content.get(counter).equals("Flaws")){
            logger.info("[Deserialization] Virtue: " + content.get(counter).substring(1));
            character.addFeature(content.get(counter).substring(1), true);
            counter++;
        }

        counter++;
        while(!content.get(counter).equals("Abilities")){
            logger.info("[Deserialization] Flaw: " + content.get(counter).substring(1));
            character.addFeature(content.get(counter).substring(1), false);
            counter++;
        }

        //Abilities
        counter++;
        while(counter < content.size()){
            String[] abilityLine = content.get(counter).substring(4).split(" ");
            if(abilityLine.length == 6){
                logger.info("[Deserialization] Ability: " + Arrays.toString(abilityLine));
                character.improveAbility(Abilities.Ability.valueOf(abilityLine[1]), Integer.parseInt(abilityLine[0]));
            }else{
                logger.info("[Deserialization] Ability: " + Arrays.toString(abilityLine));
                character.improveAbility(Abilities.Ability.valueOf(abilityLine[1]), Integer.parseInt(abilityLine[0]));
            }
            counter++;
        }

        return character;
    }

    //Currently only tests if they are equal or not via serialize
    @Override
    public int compareTo(Character o) {
        return serialize().compareTo(o.serialize());
    }
}
