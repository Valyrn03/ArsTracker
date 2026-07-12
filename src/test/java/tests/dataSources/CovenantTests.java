package tests.dataSources;

import application.data.*;
import application.models.Campaign;
import application.models.Covenant;
import application.models.CovenantFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static tests.utils.*;

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

            assertTrue(campaignDataSource.loadCovenantsFromCampaign(campaignOne).contains(covenant));
            assertFalse(campaignDataSource.loadCovenantsFromCampaign(campaignTwo).contains(covenant));
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
        void test(){
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

            campaignDataSource.addCampaign(campaign);
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
}
