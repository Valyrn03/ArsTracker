package application.data;

import application.models.*;

import java.util.List;
import java.util.Optional;

public interface IDataSource {
    void close();

    Optional<ArsCharacter> loadCharacterFromId(String characterID);

    /*
    Load campaign table in order to access details about it, getting the characters will be a separate function.
     */
    Optional<Campaign> loadCampaignFromId(String campaignID);

    boolean updateCharacter(ArsCharacter character);

    boolean updateCampaign(Campaign campaign);

    List<Campaign> getCampaigns();

    List<Ability> loadAbilitiesFromCharacter(String characterID);

    List<CharacterFeature> loadFeaturesFromCharacter(String characterID);

    Optional<Book> loadBookFromId(String bookID);

    List<CovenantFeature> loadFeaturesFromCovenant(String covenantID);

    List<Book> loadBooksFromCovenant(String covenantID);

    Optional<Covenant> loadCovenantFromId(String covenantID);
}
