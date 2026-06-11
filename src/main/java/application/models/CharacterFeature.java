package application.models;

import lombok.Getter;

import java.util.HashMap;

public class CharacterFeature implements Comparable<CharacterFeature> {
    private HashMap<String, Integer> attributes;
    @Getter private FeatureType type;
    private String name;
    private String description;
    private boolean isMajor;

    public CharacterFeature(String name, String description, boolean isVirtue, boolean isMajor){
        this.name = name;
        this.description = description;
        attributes = deriveAttribute(description);

        if(isVirtue){
            type = FeatureType.VIRTUE;
        }else{
            type = FeatureType.FLAW;
        }
        this.isMajor = isMajor;
    }

    public enum FeatureType{
        VIRTUE,
        FLAW
    }

    public int getAttribute(String attribute){
        if(!attributes.containsKey(attribute)){
            return Integer.MAX_VALUE;
        }
        return attributes.get(attribute);
    }

    public HashMap<String, Integer> deriveAttribute(String description){
        return null;
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
