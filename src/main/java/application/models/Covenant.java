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
    private Map<Art, Integer> visStores;
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

        covenant.visStores.put(Art.CREO, intMap.getOrDefault("CrVis", 0));
        covenant.visStores.put(Art.INTELLEGO, intMap.getOrDefault("InVis", 0));
        covenant.visStores.put(Art.MUTO, intMap.getOrDefault("MuVis", 0));
        covenant.visStores.put(Art.PERDO, intMap.getOrDefault("PeVis", 0));
        covenant.visStores.put(Art.REGO, intMap.getOrDefault("ReVis", 0));
        covenant.visStores.put(Art.ANIMAL, intMap.getOrDefault("AnVis", 0));
        covenant.visStores.put(Art.AURAM, intMap.getOrDefault("AuVis", 0));
        covenant.visStores.put(Art.AQUAM, intMap.getOrDefault("AqVis", 0));
        covenant.visStores.put(Art.CORPUS, intMap.getOrDefault("CoVis", 0));
        covenant.visStores.put(Art.HERBAM, intMap.getOrDefault("HeVis", 0));
        covenant.visStores.put(Art.IGNEM, intMap.getOrDefault("IgVis", 0));
        covenant.visStores.put(Art.IMAGINEM, intMap.getOrDefault("ImVis", 0));
        covenant.visStores.put(Art.MENTEM, intMap.getOrDefault("MeVis", 0));
        covenant.visStores.put(Art.TERRAM, intMap.getOrDefault("TeVis", 0));
        covenant.visStores.put(Art.VIM, intMap.getOrDefault("ViVis", 0));

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

    public static List<String> tribunals(){
        List<String> tribunals = new ArrayList<>();

        tribunals.add("Novgorod");
        tribunals.add("Rhine");
        tribunals.add("Loch Leglean");
        tribunals.add("Hibernian");
        tribunals.add("Stonehenge");
        tribunals.add("Normandy");
        tribunals.add("Provencal");
        tribunals.add("Greater Alps");
        tribunals.add("Transylvanian");
        tribunals.add("Theban");
        tribunals.add("Levant");
        tribunals.add("Roman");
        tribunals.add("Iberian");

        return tribunals;
    }
}
