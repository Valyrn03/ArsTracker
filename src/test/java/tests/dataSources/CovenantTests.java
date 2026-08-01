package tests.dataSources;

import application.data.*;
import application.models.ArsCharacter;
import application.models.Campaign;
import application.models.Covenant;
import application.models.CovenantFeature;
import application.models.enums.Art;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static tests.utils.*;

//loadCovenantVisStores implicitly tested by loading the covenant
@Slf4j
public class CovenantTests {
    IDataSource superSource;
    ICovenantDataSource dataSource;
    ICampaignDataSource campaignDataSource;
    Campaign campaign;

    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        dataSource = new CovenantDataSource(superSource);
        campaignDataSource = new CampaignDataSource(superSource);
        campaign = generateCampaign();
        campaignDataSource.addCampaign(campaign);
    }

    @Nested
    class AddCovenant{
        @Test
        void returnsTrueOnAddition(){
            Covenant covenant = generateCovenant();
            assertTrue(dataSource.addCovenant(covenant, campaign));
            log.info("New ID: {}", covenant.getId());
        }

        @Test
        void returnsFalseOnNonexistentCampaign(){
            assertFalse(dataSource.addCovenant(generateCovenant(), generateCampaign()));
        }

        @Test
        @DisplayName("adding a covenant when there are multiple campaigns")
        void addOnMultipleCampaigns(){
            Campaign campaignOne = generateCampaign();
            Campaign campaignTwo = generateCampaign();
            Covenant covenant = generateCovenant();

            campaignDataSource.addCampaign(campaignOne);
            campaignDataSource.addCampaign(campaignTwo);

            assertTrue(dataSource.addCovenant(covenant, campaignOne));

            assertTrue(dataSource.loadCovenantIdsFromCampaign(campaignOne).contains(covenant.getId()));
            assertFalse(dataSource.loadCovenantIdsFromCampaign(campaignTwo).contains(covenant.getId()));
        }

        @Test
        @DisplayName("Make sure it fails if the campaign isn't loaded into the DB")
        void returnsFalseOnUnloadedCampaign(){
            Campaign unloadedCampaign = generateCampaign();

            assertFalse(dataSource.addCovenant(generateCovenant(), unloadedCampaign));
        }
    }

    @Nested
    class UpdateVisStores{
        @Test
        void incrementSingularArt(){
            Covenant covenant = generateCovenant();
            dataSource.addCovenant(covenant, campaign);

            Random random = new Random();
            Art art = Art.values()[random.nextInt(Art.values().length)];
            covenant.addVis(art, random.nextInt(0, 20));
            assertTrue(dataSource.updateCovenantVisStores(covenant, art));

            Optional<Covenant> queriedCovenant = dataSource.loadCovenantFromId(covenant.getId());
            assertEquals(Optional.of(covenant.getVisStores()), queriedCovenant.map(Covenant::getVisStores));
        }

        @Test
        void decrementSingularArt(){
            Covenant covenant = generateCovenant();
            dataSource.addCovenant(covenant, campaign);

            Random random = new Random();
            Art art = Art.values()[random.nextInt(Art.values().length)];
            covenant.addVis(art, random.nextInt(0, 20) * -1);
            assertTrue(dataSource.updateCovenantVisStores(covenant, art));

            Optional<Covenant> queriedCovenant = dataSource.loadCovenantFromId(covenant.getId());
            assertEquals(Optional.of(covenant.getVisStores()), queriedCovenant.map(Covenant::getVisStores));
        }

        @Test
        void incrementMultipleArts(){
            fail();
        }

        @Test
        void returnsFalseOnNonexistentCovenant(){
            fail();
        }
    }

    @Nested
    class LoadBookFromId{
        @Test
        void returnsBookOnQuery(){
            fail();
        }

        @Test
        void returnsEmptyOnNonexistent(){
            fail();
        }

        @Test
        void returnsEmptyOnNull(){
            fail();
        }
    }

    @Nested
    class SaveCovenantFeature{
        @Test
        void returnsTrueOnAddition(){
            CovenantFeature feature = generateCovenantFeature();
            assertTrue(dataSource.saveCovenantFeature(feature));
            assertNotEquals(0, feature.getId());
        }

        @Test
        void returnsFalseOnNullFeature(){
            assertFalse(dataSource.saveCovenantFeature(null));
        }

    }

    @Nested
    class AddFeatureToCovenant{
        @Test
        void returnsTrueOnAddition(){
            Covenant covenant = generateCovenant();
            CovenantFeature feature = generateCovenantFeature();

            dataSource.addCovenant(covenant, campaign);
            dataSource.saveCovenantFeature(feature);

            assertTrue(dataSource.addFeatureToCovenant(covenant, feature));
        }

        @Test
        void returnsTrueOnMultipleAdditions(){
            Covenant covenant = generateCovenant();
            CovenantFeature featureOne = generateCovenantFeature();
            CovenantFeature featureTwo = generateCovenantFeature();

            dataSource.addCovenant(covenant, campaign);
            dataSource.saveCovenantFeature(featureOne);
            dataSource.saveCovenantFeature(featureTwo);

            assertTrue(dataSource.addFeatureToCovenant(covenant, featureOne));
            assertTrue(dataSource.addFeatureToCovenant(covenant, featureTwo));
        }

        @Test
        void returnsFalseOnNullCovenant(){
            assertFalse(dataSource.addFeatureToCovenant(null, generateCovenantFeature()));
        }

        @Test
        void returnsFalseOnNullFeature(){
            Covenant covenant = generateCovenant();
            dataSource.addCovenant(covenant, campaign);

            assertFalse(dataSource.addFeatureToCovenant(covenant, null));
        }

        @Test
        void returnsFalseOnNonexistentCovenant(){
            assertFalse(dataSource.addFeatureToCovenant(generateCovenant(), generateCovenantFeature()));
        }
    }

    @Nested
    class LoadCovenantFeatureFromId{
        @Test
        void returnsFeatureOnQuery(){
            CovenantFeature feature = generateCovenantFeature();

            dataSource.saveCovenantFeature(feature);

            Optional<CovenantFeature> loadedFeature = dataSource.loadCovenantFeatureFromId(feature.getId());
            assertEquals(Optional.of(feature), loadedFeature, () -> feature.toString() + "\nvs\n" + loadedFeature.map(CovenantFeature::toString));

            log.info(dataSource.toString());
        }

        @Test
        void returnsEmptyOnNonexistent(){
            CovenantFeature feature = generateCovenantFeature();

            assertEquals(Optional.empty(), dataSource.loadCovenantFeatureFromId(feature.getId()));
        }

    }

    @Nested
    class LoadFeaturesFromCovenant{
        @Test
        @DisplayName("returns a singular feature from a singular covenant")
        void returnsSingular(){
            Covenant covenant = generateCovenant();
            CovenantFeature feature = generateCovenantFeature();

            dataSource.addCovenant(covenant, campaign);
            dataSource.saveCovenantFeature(feature);
            dataSource.addFeatureToCovenant(covenant, feature);

            List<CovenantFeature> features = dataSource.loadFeaturesFromCovenant(covenant);

            assertEquals(1, features.size());
            assertTrue(features.contains(feature));
        }

        @Test
        @DisplayName("returns multiple features from a given covenant")
        void returnsMultipleFeatures(){
            Covenant covenant = generateCovenant();
            CovenantFeature featureOne = generateCovenantFeature();
            CovenantFeature featureTwo = generateCovenantFeature();

            dataSource.addCovenant(covenant, campaign);
            dataSource.saveCovenantFeature(featureOne);
            dataSource.saveCovenantFeature(featureTwo);
            dataSource.addFeatureToCovenant(covenant, featureOne);
            dataSource.addFeatureToCovenant(covenant, featureTwo);

            List<CovenantFeature> features = dataSource.loadFeaturesFromCovenant(covenant);

            assertEquals(2, features.size());
            assertTrue(features.contains(featureOne));
            assertTrue(features.contains(featureTwo));
        }

        @Test
        @DisplayName("returns empty list on null covenant")
        void returnsEmptyOnNull(){
            assertTrue(dataSource.loadFeaturesFromCovenant(null).isEmpty());
        }

        @Test
        @DisplayName("returns an empty list on a nonexistent covenant")
        void returnsEmptyOnNonexistent(){
            assertTrue(dataSource.loadFeaturesFromCovenant(generateCovenant()).isEmpty());
        }

    }

    @Nested
    class LoadBooksFromCovenant{
        @Test
        @DisplayName("returns a singular book from a singular covenant")
        void returnsBookOnQuery(){

        }

        @Test
        @DisplayName("returns multiple books from singular covenant")
        void returnsMultipleBooks(){

        }

        @Test
        @DisplayName("returns empty list on null covenant")
        void returnsEmptyOnNull(){

        }

        @Test
        @DisplayName("returns empty list on nonexistent covenant")
        void returnsEmptyOnNonexistent(){

        }
    }

    @Nested
    class LoadCovenantFromId{
        @Test
        @DisplayName("returns singular saved covenant")
        void returnsSingularCovenant(){
            Covenant covenant = generateCovenant();

            dataSource.addCovenant(covenant, campaign);
            assertEquals(Optional.of(covenant), dataSource.loadCovenantFromId(covenant.getId()));
        }

        @Test
        @DisplayName("returns singular covenant from multiple in given campaign")
        void returnsSpecificCovenant(){
            Covenant covenantOne = generateCovenant();
            Covenant covenantTwo = generateCovenant();

            dataSource.addCovenant(covenantOne, campaign);
            dataSource.addCovenant(covenantTwo, campaign);

            assertEquals(Optional.of(covenantOne), dataSource.loadCovenantFromId(covenantOne.getId()));
        }

        @Test
        @DisplayName("returns correct covenant with multiple campaigns")
        void returnsOnMultipleCampaigns(){
            Campaign campaignTwo = generateCampaign();
            campaignDataSource.addCampaign(campaignTwo);

            Covenant covenant = generateCovenant();
            dataSource.addCovenant(covenant, campaign);

            assertEquals(Optional.of(covenant), dataSource.loadCovenantFromId(covenant.getId()));
        }

        @Test
        void returnsEmptyOnNonexistentCovenant(){
            Covenant covenant = generateCovenant();
            assertEquals(Optional.empty(), dataSource.loadCovenantFromId(covenant.getId()));
        }
    }

    @Nested
    class LoadCovenantCharacters{
        ICharacterDataSource characterDataSource;
        @BeforeEach
        void setUpCDS(){
            characterDataSource = new CharacterDataSource(superSource);
        }

        @Test
        void returnsSingularCharacter(){
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter();

            dataSource.addCovenant(covenant, campaign);
            characterDataSource.addBaseCharacterToCovenant(covenant, character);

            assertEquals(1, dataSource.loadCovenantCharacters(covenant).size());
            assertEquals(character, dataSource.loadCovenantCharacters(covenant).getFirst());
        }

        @Test
        void returnsEmptyListOnNoCharacters(){
            Covenant covenant = generateCovenant();

            dataSource.addCovenant(covenant, campaign);

            assertEquals(0, dataSource.loadCovenantCharacters(covenant).size());
            assertEquals(Collections.emptyList(), dataSource.loadCovenantCharacters(covenant));
        }

        @Test
        void returnsEmptyListOnUnloadedCovenant(){
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter();

//            dataSource.addCovenant(covenant, campaign);
            characterDataSource.addBaseCharacterToCovenant(covenant, character);

            assertEquals(0, dataSource.loadCovenantCharacters(covenant).size());
            assertEquals(Collections.emptyList(), dataSource.loadCovenantCharacters(covenant));
        }

        @Test
        void returnsEmptyListOnNullCovenant(){
            assertEquals(0, dataSource.loadCovenantCharacters(null).size());
            assertEquals(Collections.emptyList(), dataSource.loadCovenantCharacters(null));
        }

        @Test
        void returnsMultipleCharacters(){
            Covenant covenant = generateCovenant();
            ArsCharacter characterOne = generateCharacter();
            ArsCharacter characterTwo = generateCharacter();

            dataSource.addCovenant(covenant, campaign);
            characterDataSource.addBaseCharacterToCovenant(covenant, characterOne);
            characterDataSource.addBaseCharacterToCovenant(covenant, characterTwo);

            assertEquals(2, dataSource.loadCovenantCharacters(covenant).size());
            assertTrue(dataSource.loadCovenantCharacters(covenant).contains(characterOne));
            assertTrue(dataSource.loadCovenantCharacters(covenant).contains(characterTwo));
        }

        @Test
        void returnsCorrectCharacterIfMultipleCovenants(){
            Covenant covenantOne = generateCovenant();
            Covenant covenantTwo = generateCovenant();
            ArsCharacter character = generateCharacter();

            dataSource.addCovenant(covenantOne, campaign);
            dataSource.addCovenant(covenantTwo, campaign);
            characterDataSource.addBaseCharacterToCovenant(covenantOne, character);

            assertEquals(1, dataSource.loadCovenantCharacters(covenantOne).size());
            assertEquals(character, dataSource.loadCovenantCharacters(covenantOne).getFirst());
            assertEquals(0, dataSource.loadCovenantCharacters(covenantTwo).size());
        }
    }

    @Nested
    class LoadCovenantsIdsFromCampaign {
        private boolean addCovenant(Covenant covenant, String campaignName){
            try(Connection connection = superSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO covenant (name, tribunal, campaign_name, establishSeason) VALUES (?, ?, ?, ?)");
                PreparedStatement statement1 = connection.prepareStatement("SELECT last_insert_rowid()")){
                statement.setString(1, covenant.getName());
                statement.setString(2, covenant.getTribunal().toString());
                statement.setString(3, campaignName);
                statement.setInt(4, covenant.getEstablishmentSeason());

                statement.execute();

                ResultSet resultSet = statement1.executeQuery();
                covenant.setId(resultSet.getInt(1));
            }catch (SQLException exception){
                log.error("{}", exception.getMessage());
                return false;
            }

            return true;
        }
        @Test
        @DisplayName("returns the singular covenant associated with the campaign")
        void returnsSingularCovenant() {
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();

            campaignDataSource.addCampaign(campaign);
            assertTrue(addCovenant(covenant, campaign.getName()));

            assertEquals(1, dataSource.loadCovenantIdsFromCampaign(campaign).size());
            assertEquals(covenant.getId(), dataSource.loadCovenantIdsFromCampaign(campaign).getFirst());
        }

        @Test
        @DisplayName("returns the covenants associated with a campaign")
        void returnsMultipleCovenants(){
            Campaign campaign = generateCampaign();
            Covenant covenantOne = generateCovenant();
            Covenant covenantTwo = generateCovenant();

            campaignDataSource.addCampaign(campaign);
            assertTrue(addCovenant(covenantOne, campaign.getName()));
            assertTrue(addCovenant(covenantTwo, campaign.getName()));

            assertEquals(2, dataSource.loadCovenantIdsFromCampaign(campaign).size());
            assertTrue(dataSource.loadCovenantIdsFromCampaign(campaign).contains(covenantOne.getId()));
            assertTrue(dataSource.loadCovenantIdsFromCampaign(campaign).contains(covenantTwo.getId()));
        }

        @Test
        @DisplayName("returns an empty list when no covenants exist for the campaign")
        void returnsEmptyListForNoCovenants() {
            Campaign campaign = generateCampaign();
            campaignDataSource.addCampaign(campaign);

            assertTrue(dataSource.loadCovenantIdsFromCampaign(campaign).isEmpty());
        }

        @Test
        @DisplayName("returns an empty list for a null campaign")
        void returnsEmptyListForNullCampaign() {
            assertTrue(dataSource.loadCovenantIdsFromCampaign(null).isEmpty());
        }

        @Test
        @DisplayName("Returns only the covenants that belong to the selected campaign")
        void returnOnlyChildren(){
            Campaign campaignOne = generateCampaign();
            Campaign campaignTwo = generateCampaign();
            Covenant covenantOne = generateCovenant();
            Covenant covenantTwo = generateCovenant();

            campaignDataSource.addCampaign(campaignOne);
            campaignDataSource.addCampaign(campaignTwo);
            assertTrue(addCovenant(covenantOne, campaignOne.getName()));
            assertTrue(addCovenant(covenantTwo, campaignTwo.getName()));

            assertEquals(1, dataSource.loadCovenantIdsFromCampaign(campaignOne).size());
            assertEquals(covenantOne.getId(), dataSource.loadCovenantIdsFromCampaign(campaignOne).getFirst());
            assertEquals(1, dataSource.loadCovenantIdsFromCampaign(campaignTwo).size());
            assertEquals(covenantTwo.getId(), dataSource.loadCovenantIdsFromCampaign(campaignTwo).getFirst());
        }
    }
}
