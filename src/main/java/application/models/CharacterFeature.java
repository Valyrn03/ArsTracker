package application.models;

import application.models.enums.Attribute;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class CharacterFeature implements Comparable<CharacterFeature> {
    @Setter @Getter private int id;
    @Getter private FeatureType type;
    @Getter private String name;
    @Getter private String description; //Purely a flavor descriptor
    @Getter private boolean isMajor;
    @Getter private List<Ability> abilities;
    @Getter private List<String> rules;

    public CharacterFeature(String name, String description, boolean isVirtue, boolean isMajor){
        this.name = name;
        this.description = description;

        if(isVirtue){
            type = FeatureType.VIRTUE;
        }else{
            type = FeatureType.FLAW;
        }
        this.isMajor = isMajor;
        abilities = new ArrayList<>();
        rules = new ArrayList<>();
    }

    public enum FeatureType{
        VIRTUE,
        FLAW
    }

    public void addAbility(Ability ability){
        this.abilities.add(ability);
    }

    public void addRule(String rule){
        this.rules.add(rule);
    }

    @Override
    public String toString(){
        return name;
    }

    /*
    Comparing Features

    Major Virtue > Minor Virtue > Major Flaw > Minor Flaw

    If != type
        return
    If != major
        return
    return name
     */
    @Override
    public int compareTo(CharacterFeature o) {
        if(this == o) {
            return 0;
        }else if(this.type != o.type){
            if(this.type == FeatureType.VIRTUE){
                return 1;
            }else{
                return -1;
            }
        }else if(this.isMajor != o.isMajor){
            if(this.isMajor){
                return 1;
            }else{
                return -1;
            }
        }
        return this.name.compareTo(o.name);
    }
}
