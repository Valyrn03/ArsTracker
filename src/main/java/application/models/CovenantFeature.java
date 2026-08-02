package application.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class CovenantFeature implements Comparable<CovenantFeature>{
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

    @Override
    public int compareTo(CovenantFeature o) {
        if(this.type.equals(FeatureType.BOON) && o.type.equals(FeatureType.HOOK)){
            return 1;
        }else if(this.type.equals(FeatureType.HOOK) && o.type.equals(FeatureType.BOON)){
            return -1;
        }else if(this.isMajor && !o.isMajor){
            return 1;
        }else if(!this.isMajor && o.isMajor){
            return -1;
        }else{
            return this.name.compareTo(o.name);
        }
    }

    public enum FeatureType{
        BOON,
        HOOK
    }

    public String toStringShortened(){
        StringBuilder builder = new StringBuilder();

        builder.append(name).append(" (");

        if(isMajor){
            builder.append("Major ");
        }else{
            builder.append("Minor ");
        }

        if(type.equals(FeatureType.BOON)){
            builder.append("Boon)");
        }else{
            builder.append("Hook)");
        }

        return builder.toString();
    }
}
