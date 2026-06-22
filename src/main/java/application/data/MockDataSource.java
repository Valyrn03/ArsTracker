package application.data;

import application.models.*;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class MockDataSource implements ICampaignDataSource, ICovenantDataSource, ICharacterDataSource, IDataSource{
    public MockDataSource(){

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
    public List<Ability> loadAbilitiesFromCharacter(ArsCharacter character) {
        return List.of();
    }

    @Override
    public List<CharacterFeature> loadFeaturesFromCharacter(ArsCharacter character) {
        return List.of();
    }

    @Override
    public Optional<Book> loadBookFromId(String bookID) {
        return Optional.empty();
    }

    @Override
    public Optional<CovenantFeature> loadCovenantFeatureFromId(String featureID) {
        return Optional.empty();
    }

    @Override
    public List<CovenantFeature> loadFeaturesFromCovenant(Covenant covenant) {
        return List.of();
    }

    @Override
    public List<Book> loadBooksFromCovenant(Covenant covenant) {
        return List.of();
    }

    @Override
    public Optional<Covenant> loadCovenantFromId(String covenantID) {
        return Optional.empty();
    }

    @Override
    public List<Covenant> loadCovenantsFromCampaign(Campaign campaign) {
        return List.of();
    }

    @Override
    public boolean updateCovenant(Covenant covenant) {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
