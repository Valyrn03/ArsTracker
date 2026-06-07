package application.models;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

public class Character implements Serializable, Comparable<Character> {

    @Getter private String name;
    private Map<Attribute, Integer> baseAttributes;
    //Ability <-> XP
    @Getter private List<Ability> abilities;
    private Map<ExtraneousAttribute, Integer> attributes;
    private String campaign;
    @Getter private CharacterType characterType;
    @Getter ArrayList<CharacterFeature> features;
    @Getter UUID id;

    private Character(){
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new ArrayList<>();
        features = new ArrayList<>();
    }

    /*
    Build a Character from the data found in the Character table plus the already known campaign.

    There must be a campaign for the character to exist
     */
    public static Character buildCharacterFromMap(Map<String, String> map, Campaign campaign){
        Character character = new Character();

        character.id = UUID.fromString(map.get("id"));
        character.name = map.get("name");
        character.campaign = map.get("campaign");

        character.attributes.put(ExtraneousAttribute.AGE, campaign.currentSeason - Integer.parseInt(map.get("birth_season")));
        character.characterType = CharacterType.valueOf(map.get("character_type"));

        for(Attribute attribute: Attribute.values()){
            character.baseAttributes.put(attribute, Integer.parseInt(map.get(attribute.name().toLowerCase())));
        }

        return character;
    }

    public static Character buildCharacter(String name, int birthSeason, String type){
        return null;
    }

    public static enum CharacterType {
        MAGUS,
        COMPANION,
        GROG
    }

    private void setDefaultAttributes(){
        for(Attribute attribute : Attribute.values()){
            baseAttributes.put(attribute, 0);
        }
    }

    public void setAttributes(ArrayList<Integer> characteristics){
        Attribute[] attributes = Attribute.values();
        for(int i = 0; i < characteristics.size(); i++){
            baseAttributes.put(attributes[i], characteristics.get(i));
        }
    }

    public int getAttribute(Attribute attribute){
        return baseAttributes.getOrDefault(attribute, Integer.MAX_VALUE);
    }

    public int getAttribute(ExtraneousAttribute attribute){
        return attributes.getOrDefault(attribute, Integer.MAX_VALUE);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        //Name, Type, Age, Characteristics, Abilities, Virtues & Flaws
        builder.append(name).append("\n\n").append(characterType.toString());
        if(baseAttributes.containsKey(ExtraneousAttribute.AGE)){
            builder.append("(").append(baseAttributes.get(ExtraneousAttribute.AGE).toString()).append(")\n\n");
        }

        builder.append("CHARACTERISTICS\n");
        for(Map.Entry<String, Integer> attribute : getAttributes().entrySet()){
            builder.append("\t")
                    .append(attribute.getKey())
                    .append(": ")
                    .append(attribute.getValue().toString())
                    .append("\n");
        }

        builder.append("\nABILITIES\n");
        for(Ability ability: abilities){
            builder.append("\t").append(ability.toString());
        }

        if(features.isEmpty()){
            return "ERROR: Character Features Not Found";
        }

        ArrayList<CharacterFeature> flaws = new ArrayList<>();

        builder.append("\nVIRTUES\n");
        for(CharacterFeature feature : features){
            if(feature.isVirtue()){
                builder.append("\t")
                        .append(feature.toString())
                        .append("\n");
            }else{
                flaws.add(feature);
            }
        }

        builder.append("FLAWS\n");
        for(CharacterFeature feature : flaws){
            builder.append("\t")
                    .append(feature.toString())
                    .append("\n");
        }

        return builder.toString();
    }

    public HashMap<String, Integer> getAttributes(){
        HashMap<String, Integer> map = new HashMap<>();

        for(Attribute attribute : Attribute.values()){
            int value = baseAttributes.get(attribute);
            map.put(String.valueOf(attribute), value);
        }

        return map;
    }

    public void addFeature(String feature, boolean isVirtue, boolean isMajor){
        features.add(new CharacterFeature(feature, "", isVirtue, isMajor));
    }

    public void addFeature(CharacterFeature feature){
        features.add(feature);
    }

    public String serialize(){
        return null;
    }

    /*
    Input: ArrayList of Lines
    Structure:
        Name
        Characteristics/Attributes
        Virtues
            *
        Flaws
            *
        Abilities
            *
     */
    public static Character deserialize(ArrayList<String> content){
        return null;
    }

    //Currently only tests if they are equal or not via serialize
    //Character Parts: Name, BaseAttributes, Attributes, Abilities, Type, Features
    @Override
    public int compareTo(Character o) {
        return getName().compareTo(o.getName()) | getCharacterType().compareTo(o.getCharacterType());
    }
}
