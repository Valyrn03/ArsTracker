package application.models;

import application.models.enums.Art;
import application.models.enums.Attribute;
import application.models.enums.ExtraneousAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

@Slf4j
//@ToString
@EqualsAndHashCode
public class ArsCharacter implements Serializable, Comparable<ArsCharacter> {
    @Setter @Getter private int id;
    @Getter private String name;
    private Map<Attribute, Integer> baseAttributes;
    //Ability <-> XP
    @Getter private List<Ability> abilities;
    private Map<ExtraneousAttribute, Integer> attributes;
    @Getter private CharacterType characterType;
    @Getter ArrayList<CharacterFeature> features;
    private Map<Art, Integer> arts;

    private ArsCharacter(){
        baseAttributes = new HashMap<>();
        attributes = new HashMap<>();
        attributes.put(ExtraneousAttribute.SIZE, 0);
        setDefaultAttributes();
        abilities = new ArrayList<>();
        features = new ArrayList<>();
        arts = new HashMap<>();
    }

    /*
    Build a Character from the data found in the Character table plus the already known campaign.

    There must be a campaign for the character to exist
     */
    public static ArsCharacter buildCharacterFromMap(Map<String, String> map){
        ArsCharacter character = new ArsCharacter();

        if(!map.get("id").equals("0")){
            character.id = Integer.parseInt(map.get("id"));
        }else{
            character.id = 0;
        }
        character.name = map.get("name");

        character.attributes.put(ExtraneousAttribute.BIRTH_SEASON, Integer.parseInt(map.get("birth_season")));

        character.characterType = CharacterType.valueOf(map.get("character_type"));

        for(Attribute attribute: Attribute.values()){
            character.baseAttributes.put(attribute, Integer.parseInt(map.get(attribute.name().toLowerCase())));
        }

        return character;
    }

    public static ArsCharacter buildCharacter(String name, int birthSeason, String type){
        return null;
    }

    public enum CharacterType {
        MAGUS (0),
        COMPANION (1),
        GROG (2);

        private final int id;
        CharacterType(int i) {
            this.id = i;
        }

        public int id(){
            return this.id;
        }
    }

    private void setDefaultAttributes(){
        for(Attribute attribute : Attribute.values()){
            baseAttributes.put(attribute, 0);
        }
    }

    public void setAttributes(Map<Attribute, Integer> map){
        baseAttributes.putAll(map);
    }

    public int getAttribute(Attribute attribute){
        return baseAttributes.getOrDefault(attribute, Integer.MAX_VALUE);
    }

    public int getAttribute(ExtraneousAttribute attribute){
        return attributes.getOrDefault(attribute, Integer.MAX_VALUE);
    }

    public HashMap<String, Integer> getAttributes(){
        HashMap<String, Integer> map = new HashMap<>();

        for(Attribute attribute : Attribute.values()){
            int value = baseAttributes.get(attribute);
            map.put(String.valueOf(attribute), value);
        }

        return map;
    }

    public String toStringShortened(){
        return this.name + " (" + this.characterType.toString() + ")";
    }

    public void addFeature(String feature, boolean isVirtue, boolean isMajor){
        features.add(new CharacterFeature(feature, "", isVirtue, isMajor));
    }

    public void addFeature(CharacterFeature feature){
        features.add(feature);
    }

    public void addAbility(Ability ability){
        abilities.add(ability);
        abilities.sort(null);
    }

    public void setArts(Map<Art, Integer> map){
        if(!characterType.equals(CharacterType.MAGUS)){
            return;
        }

        arts.putAll(map);
    }

    public void incrementArt(Art art, int increment){
        if(!characterType.equals(CharacterType.MAGUS)){
            return;
        }

        if(arts.containsKey(art)){
            arts.put(art, arts.get(art) + increment);
        }else{
            arts.put(art, increment);
        }
    }

    public int getArt(Art art){
        if(!characterType.equals(CharacterType.MAGUS)){
            return 0;
        }

        return arts.getOrDefault(art, 0);
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
    public static ArsCharacter deserialize(ArrayList<String> content){
        return null;
    }

    //Currently only tests if they are equal or not via serialize
    //Character Parts: Name, BaseAttributes, Attributes, Abilities, Type, Features
    @Override
    public int compareTo(ArsCharacter o) {
        return getName().compareTo(o.getName()) | getCharacterType().compareTo(o.getCharacterType());
    }
}
