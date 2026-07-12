package application.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@ToString
@EqualsAndHashCode
public class CovenantFeature {
    @Getter @Setter int id;
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

    public CovenantFeature(int i_id){
        this.id = i_id;
    }

    /*
    Constructor to be used when a new feature is created
     */
    public CovenantFeature(){
        this.id = 0;
    }

    public enum FeatureType{
        BOON,
        HOOK
    }
}
