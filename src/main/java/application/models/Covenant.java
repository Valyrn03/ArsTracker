package application.models;

import application.models.enums.Art;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class Covenant {
    @Getter private List<Character> playerCharacters;
    @Getter private List<Character> nonPlayerCharacters;

    private int establishmentSeason;
    private List<Book> books;
    private record LabTexts(List<Spell> spells, List<EnchantedItem> items){}
    private Map<Art, Integer> visStores;
    private List<CovenantFeature> features;
}
