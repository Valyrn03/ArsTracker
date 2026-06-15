package application.data;

import application.models.*;

import java.util.List;
import java.util.Optional;

public class MockDataSource implements IDataSource{
    public MockDataSource(){

    }

    @Override
    public void close() {

    }

    @Override
    public Optional<ArsCharacter> loadCharacterFromId(String characterID) {
        return Optional.empty();
    }

    @Override
    public Optional<Campaign> loadCampaignFromId(String campaignID) {
        return Optional.empty();
    }

    @Override
    public boolean updateCharacter(ArsCharacter character) {
        return false;
    }

    @Override
    public boolean updateCampaign(Campaign campaign) {
        return false;
    }

    @Override
    public List<Campaign> getCampaigns() {
        return List.of();
    }

    @Override
    public List<Ability> loadAbilitiesFromCharacter(String characterID) {
        return List.of();
    }

    @Override
    public List<CharacterFeature> loadFeaturesFromCharacter(String characterID) {
        return List.of();
    }

    @Override
    public Optional<Book> loadBookFromId(String bookID) {
        return Optional.empty();
    }
}
