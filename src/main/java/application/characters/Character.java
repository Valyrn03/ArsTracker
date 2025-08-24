package application.characters;

import application.utils.CharacterUtils;
import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.io.Serializable;
import java.util.*;

import static java.util.Collections.sort;

//Random comment

public class Character implements Serializable, Comparable<Character> {
    final Logger logger = LoggerFactory.getLogger(Character.class);

    private String name;
    private Map<Attribute, Integer> baseAttributes;
    //Ability <-> XP
    private List<Ability> abilities;
    private Map<ExtraneousAttribute, Integer> attributes;
    private CharacterType characterType;
    ArrayList<CharacterFeature> features;
    UUID id;

    public Character(String name, int age, String characterCategory){
        this.name = name;
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(ExtraneousAttribute.AGE, age);
        attributes.put(ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new ArrayList<>();
        id = UUID.randomUUID();
        characterType = CharacterType.valueOf(characterCategory.toUpperCase());
        features = new ArrayList<>();
    }

    public Character(String name, String characterCategory){
        this.name = name;
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new ArrayList<>();
        id = UUID.randomUUID();
        characterType = CharacterType.valueOf(characterCategory.toUpperCase());
        features = new ArrayList<>();
    }

    public Character(String name, CharacterType characterCategory){
        this.name = name;
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new ArrayList<>();
        id = UUID.randomUUID();
        characterType = characterCategory;
        features = new ArrayList<>();
    }

    public UUID getID() {
        return id;
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

    public String getName(){
        return name;
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
        builder.append(getName()).append("\n\n").append(characterType.toString());
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
        for(Ability ability: getAbilities()){
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

    public List<Ability> getAbilities(){
        return abilities;
    }

    public ArrayList<CharacterFeature> getFeatures(){
        return features;
    }

    public void addFeature(String feature, boolean isVirtue, boolean isMajor){
        features.add(new CharacterFeature(feature, "", isVirtue, isMajor));
    }

    public void addFeature(CharacterFeature feature){
        features.add(feature);
    }

    public CharacterType getType(){
        return characterType;
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
        return getName().compareTo(o.getName()) | getType().compareTo(o.getType());
    }
}
