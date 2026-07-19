package application.data;

import application.models.Ability;
import application.models.ArsCharacter;
import application.models.CharacterFeature;
import application.models.Covenant;

import java.util.List;
import java.util.Optional;

public interface ICharacterDataSource {
    Optional<ArsCharacter> loadBaseCharacterFromId(int characterID);

    boolean updateCharacterCharacteristics(ArsCharacter character);

    List<Ability> loadAbilities(int id);

    List<CharacterFeature> loadFeaturesFromCharacter(ArsCharacter character);

    Optional<CharacterFeature> loadFeatureFromId(int featureId);

    boolean addBaseCharacterToCovenant(Covenant covenant, ArsCharacter character);

    boolean addAbility(int id, Ability ability);

    boolean addFeatureToCharacter(ArsCharacter character, CharacterFeature feature);

    boolean saveNewFeature(CharacterFeature feature);

    Optional<Ability> loadAbilityFromId(int ownerId, String ability);
}
