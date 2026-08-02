package application.models;

import application.models.enums.Art;
import application.models.enums.Tribunal;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Covenant implements Comparable<Covenant>{
    private record LabTexts(List<Spell> spells, List<EnchantedItem> items){}

    @Setter @Getter private int id;
    @Getter private Tribunal tribunal;
    @Getter private String name;
    @Getter private int establishmentSeason;

    @Getter private List<ArsCharacter> playerCharacters;
//    @Getter private List<ArsCharacter> nonPlayerCharacters;

    @Getter private List<Book> books;
    private LabTexts labTexts;
    @Setter @Getter private Map<Art, Integer> visStores;
    @Getter private List<CovenantFeature> features;

    public List<ArsCharacter> accessedCharacters;

    private Covenant(){
        this.playerCharacters = new ArrayList<>();
        this.books = new ArrayList<>();
        this.labTexts = new LabTexts(new ArrayList<>(), new ArrayList<>());

        this.visStores = new HashMap<>();
        for(Art art : Art.values()){
            this.visStores.put(art, 0);
        }

        this.accessedCharacters = new ArrayList<>();
        this.features = new ArrayList<>();
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

    public void addFeature(CovenantFeature feature){
        this.features.add(feature);
        this.features.sort(null);
    }

    public void addFeature(List<CovenantFeature> feature){
        this.features.addAll(feature);
        this.features.sort(null);
    }

    public void addVis(Art art, int valueChange){
        visStores.put(art, visStores.get(art) + valueChange);
    }

    public void updateVis(Art art, int newValue){
        visStores.put(art, newValue);
    }

    public void addCharacter(List<ArsCharacter> characters){
        for(ArsCharacter character : characters){
            if(!playerCharacters.contains(character)){
                playerCharacters.add(character);
            }
        }

        playerCharacters.sort(null);
    }

    public void addCharacter(ArsCharacter character){
        if(!playerCharacters.contains(character)){
            playerCharacters.add(character);
        }

        playerCharacters.sort(null);
    }

    public int getVis(Art art){
        return visStores.get(art);
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

    /**
     * Natural ordering: by name (case-insensitive), then establishment season,
     * then id, so that covenants sort in a sensible, stable order for
     * automated sorting (e.g. Collections.sort, TreeSet, Stream.sorted()).
     */
    @Override
    public int compareTo(Covenant other){
        int nameCompare = String.CASE_INSENSITIVE_ORDER.compare(this.name, other.name);
        if(nameCompare != 0){
            return nameCompare;
        }

        int seasonCompare = Integer.compare(this.establishmentSeason, other.establishmentSeason);
        if(seasonCompare != 0){
            return seasonCompare;
        }

        return Integer.compare(this.id, other.id);
    }
}
