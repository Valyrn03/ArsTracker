package application.models;

import lombok.Getter;

import java.util.Map;

public class CovenantFeature {
    @Getter private String name;
    @Getter private FeatureType type;
    @Getter boolean isMajor;
    @Getter String description;

    public CovenantFeature(Map<String, String> map){
        this.name = map.get("name");
        this.type = FeatureType.valueOf(map.get("type"));
        this.isMajor = Boolean.parseBoolean(map.get("isMajor"));
        this.description = map.get("description");
    }

    public enum FeatureType{
        BOON,
        HOOK
    }
}
