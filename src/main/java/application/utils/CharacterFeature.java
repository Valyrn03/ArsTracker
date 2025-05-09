package application.utils;

import java.util.HashMap;

public class CharacterFeature implements Comparable<CharacterFeature> {
    private HashMap<String, Integer> attributes;
    private boolean isVirtue;
    private String name;
    private String description;

    public CharacterFeature(String name, String description, boolean virtue){
        this.name = name;
        this.description = description;
        attributes = deriveAttribute(description);
        isVirtue = virtue;
    }

    public int getAttribute(String attribute){
        if(!attributes.containsKey(attribute)){
            return Integer.MAX_VALUE;
        }
        return attributes.get(attribute);
    }

    public boolean isVirtue(){
        return isVirtue;
    }

    public boolean isFlaw(){
        return !isVirtue;
    }

    public HashMap<String, Integer> deriveAttribute(String description){
        return null;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public int compareTo(CharacterFeature o) {
        return this.name.compareTo(o.name);
    }
}
