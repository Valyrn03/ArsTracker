package application.models;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public class Covenant {
    @Getter private List<Character> playerCharacters;
    @Getter private List<Character> nonPlayerCharacters;

    private int establishmentSeason;
    private List<Book> books;
    private List<LabText> labTexts;
    private Map<Art, Integer> visStores;
    private List<CovenantFeature> features;
}
