package application.models;

import application.models.enums.Art;
import application.models.enums.Tribunal;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Covenant {
    private record LabTexts(List<Spell> spells, List<EnchantedItem> items){}

    @Setter @Getter private int id;
    @Getter private Tribunal tribunal;
    @Getter private String name;
    @Getter private int establishmentSeason;

    @Getter private List<Character> playerCharacters;
    @Getter private List<Character> nonPlayerCharacters;

    private List<Book> books;
    private LabTexts labTexts;
    @Setter @Getter private Map<Art, Integer> visStores;
    private List<CovenantFeature> features;

    public List<ArsCharacter> accessedCharacters;

    private Covenant(){
        this.playerCharacters = new ArrayList<>();
        this.nonPlayerCharacters = new ArrayList<>();
        this.books = new ArrayList<>();
        this.labTexts = new LabTexts(new ArrayList<>(), new ArrayList<>());

        this.visStores = new HashMap<>();
        for(Art art : Art.values()){
            this.visStores.put(art, 0);
        }

        this.accessedCharacters = new ArrayList<>();
    }

    public static Covenant buildCovenantFromMap(Map<String, String> stringMap, Map<String, Integer> intMap){
        Covenant covenant = new Covenant();
        covenant.id = intMap.get("id");
        covenant.name = stringMap.get("name");
        covenant.tribunal = Tribunal.valueOf(stringMap.get("tribunal"));
        covenant.establishmentSeason = intMap.get("establishSeason");

        return covenant;
    }

    public static Covenant buildCovenant(String name, String tribunal, int season, Map<Art, Integer> vis){
        Covenant covenant = new Covenant();

        covenant.name = name;
        covenant.tribunal = Tribunal.valueOf(tribunal);
        covenant.establishmentSeason = season;
        covenant.visStores = vis;

        return covenant;
    }

    public static Covenant buildCovenant(String id){
        Covenant covenant = new Covenant();

        return covenant;
    }

    public void addFeature(CovenantFeature feature){
        this.features.add(feature);
    }

    public void addFeature(List<CovenantFeature> feature){
        this.features.addAll(feature);
    }

    public void addVis(Art art, int valueChange){
        visStores.put(art, visStores.get(art) + valueChange);
    }

    public void updateVis(Art art, int newValue){
        visStores.put(art, newValue);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Covenant other)){
            return false;
        }

        if(this.id != other.id){
            return false;
        }

        if(!this.tribunal.equals(other.tribunal)){
            return false;
        }

        if(!this.name.equals(other.name)){
            return false;
        }

        if(this.establishmentSeason != other.establishmentSeason){
            return false;
        }

        return this.visStores.equals(other.visStores);
    }

    public int getVis(Art art){
        return visStores.get(art);
    }
}
