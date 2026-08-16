package application.data;

import application.models.ArsCharacter;
import application.models.CharacterFeature;
import application.models.Covenant;
import application.models.enums.Art;

import java.util.List;
import java.util.Optional;

public interface ICharacterDataSource {
    Optional<ArsCharacter> loadBaseCharacterFromId(int characterID);

    boolean updateCharacterCharacteristics(ArsCharacter character);

    List<CharacterFeature> loadFeaturesFromCharacter(ArsCharacter character);

    Optional<CharacterFeature> loadFeatureFromId(int featureId);

    boolean addCharacterToCovenant(ArsCharacter character, Covenant covenant);

    boolean addFeatureToCharacter(ArsCharacter character, CharacterFeature feature);

    boolean saveNewFeature(CharacterFeature feature);

    /*
    To be precise, loads the basic character requirements (id, name, season, type, and characteristics).

    Anything else will be handled by CharacterDataSource
     */
    List<ArsCharacter> loadCovenantCharacters(Covenant covenant);

    public boolean updateCharacterArts(ArsCharacter character);

    public int loadCharacterArt(ArsCharacter character, Art art);
}
