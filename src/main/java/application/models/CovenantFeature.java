package application.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

public class CovenantFeature {
    @Getter int id;
    @Getter @Setter String name;
    @Getter @Setter FeatureType type;
    @Getter @Setter boolean isMajor;
    @Getter @Setter String description;

    public CovenantFeature(Map<String, String> map){
        this.id = Integer.parseInt(map.get("id"));
        this.name = map.get("name");
        this.isMajor = map.get("isMajor").equals("0");
        this.description = map.get("description");

        if(map.get("isBoon").equals("0")){
            this.type = FeatureType.BOON;
        }else{
            this.type = FeatureType.HOOK;
        }
    }

    public enum FeatureType{
        BOON,
        HOOK
    }
}
