package application.models;

import application.models.enums.Art;
import application.models.enums.Tribunal;
import lombok.Getter;

import java.util.*;

public class Covenant {
    private record LabTexts(List<Spell> spells, List<EnchantedItem> items){}

    @Getter private UUID id;
    @Getter private Tribunal tribunal;
    @Getter private String name;
    private int establishmentSeason;

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
        covenant.id = UUID.fromString(stringMap.get("id"));
        covenant.name = stringMap.get("name");
        covenant.tribunal = Tribunal.valueOf(stringMap.get("tribunal"));
        covenant.establishmentSeason = intMap.get("establishSeason");

        covenant.visStores.put(Art.CREO, intMap.get("CrVis"));
        covenant.visStores.put(Art.INTELLEGO, intMap.get("InVis"));
        covenant.visStores.put(Art.MUTO, intMap.get("MuVis"));
        covenant.visStores.put(Art.PERDO, intMap.get("PeVis"));
        covenant.visStores.put(Art.REGO, intMap.get("ReVis"));
        covenant.visStores.put(Art.ANIMAL, intMap.get("AnVis"));
        covenant.visStores.put(Art.AURAM, intMap.get("AuVis"));
        covenant.visStores.put(Art.AQUAM, intMap.get("AqVis"));
        covenant.visStores.put(Art.CORPUS, intMap.get("CoVis"));
        covenant.visStores.put(Art.HERBAM, intMap.get("HeVis"));
        covenant.visStores.put(Art.IGNEM, intMap.get("IgVis"));
        covenant.visStores.put(Art.IMAGINEM, intMap.get("ImVis"));
        covenant.visStores.put(Art.MENTEM, intMap.get("MeVis"));
        covenant.visStores.put(Art.TERRAM, intMap.get("TeVis"));
        covenant.visStores.put(Art.VIM, intMap.get("ViVis"));

        return covenant;
    }

    public void addFeature(CovenantFeature feature){
        this.features.add(feature);
    }

    public void addFeature(List<CovenantFeature> feature){
        this.features.addAll(feature);
    }
}
