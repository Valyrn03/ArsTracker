package tests.dataSources;

import application.data.*;
import application.models.Campaign;
import application.models.Covenant;
import application.models.enums.Art;
import application.models.enums.Tribunal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static tests.utils.generateCampaign;
import static tests.utils.generateCovenant;

public class CovenantTests {
    IDataSource superSource;
    ICovenantDataSource dataSource;
    ICampaignDataSource campaignDataSource;
    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        dataSource = new CovenantDataSource(superSource);
        campaignDataSource = new CampaignDataSource(superSource);
    }

    @Nested
    class AddCovenant{
        @Test
        void returnsTrueOnAddition(){
            Covenant covenant = generateCovenant();
            Campaign campaign = generateCampaign();

            campaignDataSource.addCampaign(campaign.getName(), campaign.getCurrentSeason());
            assertTrue(dataSource.addCovenant(covenant, campaign));
        }

        @Test
        void returnsFalseOnNullCampaign(){
            assertFalse(dataSource.addCovenant(generateCovenant(), null));
        }

        @Test
        void returnsFalseOnNonexistentCampaign(){
            assertFalse(dataSource.addCovenant(generateCovenant(), generateCampaign()));
        }

        @Test
        void returnsFalseOnNullCovenant(){
            Campaign campaign = generateCampaign();
            campaignDataSource.addCampaign(campaign);
            assertFalse(dataSource.addCovenant(null, campaign));
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
    }

    @Nested
    class UpdateCovenant{
        @Test
        void returnsTrueOnUpdate(){

        }

        @Test
        void returnsFalseOnNullCovenant(){

        }

        @Test
        void returnsFalseOnNonexistentCovenant(){

        }
    }

    @Nested
    class LoadBookFromId{
        @Test
        void returnsBookOnQuery(){

        }

        @Test
        void returnsEmptyOnNonexistent(){

        }

        @Test
        void returnsEmptyOnNull(){

        }
    }

    @Nested
    class LoadCovenantFeatureFromId{
        @Test
        void returnsFeatureOnQuery(){

        }

        @Test
        void returnsEmptyOnNonexistent(){

        }

        @Test
        void returnsEmptyOnNull(){

        }
    }

    @Nested
    class LoadFeaturesFromCovenant{
        @Test
        @DisplayName("returns a singular feature from a singular covenant")
        void returnsSingular(){

        }

        @Test
        @DisplayName("returns multiple features from a given covenant")
        void returnsMultipleFeatures(){

        }

        @Test
        @DisplayName("returns empty list on null covenant")
        void returnsEmptyOnNull(){

        }

        @Test
        @DisplayName("returns an empty list on a nonexistent covenant")
        void returnsEmptyOnNonexistent(){

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

        }

        @Test
        @DisplayName("returns singular covenant from multiple in given campaign")
        void returnsSpecificCovenant(){

        }

        @Test
        @DisplayName("returns correct covenant with multiple campaigns")
        void returnsOnMultipleCampaigns(){

        }

        @Test
        void returnsEmptyOnNullCovenant(){

        }

        @Test
        void returnsEmptyOnNullCampaign(){

        }

        @Test
        void returnsEmptyOnNonexistentCovenant(){

        }
    }
}
