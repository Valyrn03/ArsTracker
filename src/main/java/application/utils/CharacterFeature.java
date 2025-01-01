package application.utils;

import java.util.HashMap;

public abstract class CharacterFeature {
    private HashMap<String, Integer> attributes;
    private boolean isVirtue;

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
}
