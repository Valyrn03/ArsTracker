package application.data;

import application.models.Ability;
import application.models.ArsCharacter;
import application.models.CharacterFeature;

import java.util.List;
import java.util.Optional;

public interface ICharacterDataSource {
    Optional<ArsCharacter> loadCharacterFromId(String characterID);

    boolean updateCharacter(ArsCharacter character);

    List<Ability> loadAbilitiesFromCharacter(ArsCharacter character);

    List<CharacterFeature> loadFeaturesFromCharacter(ArsCharacter character);
}
