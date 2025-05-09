package application.characters;

import application.utils.Abilities;
import application.utils.CharacterFeature;
import application.utils.characterUtils;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//Random comment

public class CharacterBase implements Serializable {
    private String name;
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

    public CharacterBase(String name, int age, String characterCategory){
        this.name = name;
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(characterUtils.ExtraneousAttribute.AGE, age);
        attributes.put(characterUtils.ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new HashMap<>();
        specialities = new HashMap<>();
        id = UUID.randomUUID();
        characterType = new HashMap<>();
        if(characterCategory.equals("magus")){
            characterType.put("Magus", true);
            characterType.put("Companion", false);
            characterType.put("Grog", false);
        }else if(characterCategory.equals("companion")){
            characterType.put("Magus", false);
            characterType.put("Companion", true);
            characterType.put("Grog", false);
        }else{
            characterType.put("Magus", false);
            characterType.put("Companion", false);
            characterType.put("Grog", true);
        }
        features = new ArrayList<>();
        logger = Logger.getLogger(this.name);
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

    public void addSpeciality(Abilities.Ability ability, String speciality){
        specialities.put(ability, speciality);
    }

    @Override
    public String toString(){
        return getName();
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
            int value = baseAttributes.get(attribute);
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

    public void addFeature(String feature, boolean isVirtue){
        features.add(new CharacterFeature(feature, "", isVirtue));
    }

    public String getType(){
        if(characterType.get("Magus")){
            return "magus";
        }else if(characterType.get("Companion")){
            return "companion";
        }else{
            return "grog";
        }
    }

    public static String serialize(CharacterBase character){
        StringBuilder builder = new StringBuilder();
        builder.append(character.getName()).append("\n");
        builder.append(character.getAttribute(characterUtils.ExtraneousAttribute.AGE)).append(" ").append(character.getType()).append("\n");

        ArrayList<CharacterFeature> flaws = new ArrayList<>();

        builder.append("Virtues").append("\n");
        for(CharacterFeature feature : character.getFeatures()){
            if(feature.isVirtue()){
                builder.append("\t").append(feature.toString()).append("\n");
            }else{
                flaws.add(feature);
            }
        }

        builder.append("Flaws").append("\n");
        for(CharacterFeature feature : flaws){
            builder.append("\t").append(feature.toString()).append("\n");
        }

        builder.append("Abilities").append("\n");
        for(Map.Entry<Abilities.Ability, Integer> ability : character.getAbilities().entrySet()){
            builder.append("\t").append(ability.getValue()).append(" ").append(ability.getKey().name()).append("\n");
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
    public static CharacterBase deserialize(ArrayList<String> content){
        //CREATING CHARACTER
        String name = content.getFirst();
        String[] ageAndType = content.get(1).split(" ");
        CharacterBase character = new CharacterBase(name, Integer.parseInt(ageAndType[0]), ageAndType[1]);

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
        while(content.get(counter).startsWith(" ")){
            logger.info("[Deserialization] Virtue: " + Arrays.toString(content.get(counter).split(" ")));
            character.addFeature(content.get(counter), true);
            counter++;
        }

        counter++;
        while(content.get(counter).startsWith(" ")){
            logger.info("[Deserialization] Flaw: " + Arrays.toString(content.get(counter).split(" ")));
            character.addFeature(content.get(counter), false);
            counter++;
        }

        //Abilities
        counter++;
        while(counter < content.size()){
            String[] abilityLine = content.get(counter).split(" ");
            if(abilityLine.length == 6){
                logger.info("[Deserialization] Ability: " + Arrays.toString(abilityLine));
                character.improveAbility(Abilities.Ability.valueOf(abilityLine[5]), Integer.parseInt(abilityLine[4]));
            }else{
                logger.info("[Deserialization] Ability: " + Arrays.toString(abilityLine));
                character.improveAbility(Abilities.Ability.valueOf(abilityLine[5]), Integer.parseInt(abilityLine[4]));
            }
            counter++;
        }

        return character;
    }
}
