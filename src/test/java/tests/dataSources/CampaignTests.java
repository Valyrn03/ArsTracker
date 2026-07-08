package tests.dataSources;

import application.data.CampaignDataSource;
import application.data.IDataSource;
import application.data.MockDataSource;
import application.models.Campaign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tests.utils;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CampaignTests {
    IDataSource superSource;
    CampaignDataSource dataSource;
    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        dataSource = new CampaignDataSource(superSource);
    }

    @Nested
    @DisplayName("tests relating to adding a new campaign")
    class AddCampaign {
        @Test
        @DisplayName("add a new campaign, with all fields filled out and correct")
        void testAddCampaign(){
            Campaign campaign = utils.generateCampaign();

            boolean result = dataSource.addCampaign(campaign);

            assertTrue(result);
            assertEquals(1, dataSource.getCampaigns().size());
            assertTrue(dataSource.getCampaigns().contains(campaign));
        }

        @Test
        @DisplayName("should be returning false when a campaign with the same id or name already exists")
        void testAddCampaignSameName(){
            Campaign baseCampaign = utils.generateCampaign();
            Campaign duplicateID = Campaign.createCampaign(baseCampaign.id, String.valueOf(UUID.randomUUID()), baseCampaign.getCurrentSeason());
            Campaign duplicateName = Campaign.createCampaign(UUID.randomUUID(), baseCampaign.getName(), baseCampaign.getCurrentSeason());

            assertTrue(dataSource.addCampaign(baseCampaign));

            assertFalse(dataSource.addCampaign(duplicateID));
            assertEquals(1, dataSource.getCampaigns().size());
            assertEquals(baseCampaign, dataSource.getCampaigns().getFirst());

            assertFalse(dataSource.addCampaign(duplicateName));
            assertEquals(1, dataSource.getCampaigns().size());
            assertEquals(baseCampaign, dataSource.getCampaigns().getFirst());
        }

        @Test
        void testNullCampaign(){
            assertFalse(dataSource.addCampaign(null));
        }

        @Test
        void testAddCampaignNegativeAge(){
            UUID id = UUID.randomUUID();
            Random random = new Random();
            Campaign campaign = Campaign.createCampaign(id, id.toString(), random.nextInt(-10000, 0));

            assertFalse(dataSource.addCampaign(campaign));
        }
    }

    @Nested
    @DisplayName("loadCampaignFromId")
    class LoadCampaignFromId{
        @Test
        @DisplayName("Test with a singular known campaign")
        void returnsExistingCampaign(){
            Campaign campaign = utils.generateCampaign();

            dataSource.addCampaign(campaign);

            Optional<Campaign> result = dataSource.loadCampaignFromId(campaign.id);

            assertTrue(result.isPresent());
            assertEquals(campaign, result.get());
        }

        @Test
        @DisplayName("Correctly returns an empty object when the campaign does not exist")
        void returnsEmpty(){
            Optional<Campaign> result = dataSource.loadCampaignFromId(UUID.randomUUID());
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("updateCampaign")
    class UpdateCampaign{
        @Test
        @DisplayName("Updates an existing campaign and returns true")
        void updateExistingCampaign() throws CloneNotSupportedException {
            Campaign baseCampaign = utils.generateCampaign();
            dataSource.addCampaign(baseCampaign);

            baseCampaign.advanceSeason();
            dataSource.updateCampaign(baseCampaign);

            Optional<Campaign> newCampaign = dataSource.loadCampaignFromId(baseCampaign.id);
            assertTrue(newCampaign.isPresent());
            assertEquals(baseCampaign, newCampaign.get());
        }

        @Test
        @DisplayName("returns false on a nonexistent campaign with no saved campaigns")
        void rejectsUnknownCampaignWithNoSaved(){
            Campaign campaign = utils.generateCampaign();
            assertTrue(dataSource.updateCampaign(campaign));
        }

        @Test
        @DisplayName("returns false on a nonexistent campaign")
        void rejectsUnknownCampaign(){
            Campaign campaignOne = utils.generateCampaign();
            Campaign campaignTwo = utils.generateCampaign();

            dataSource.addCampaign(campaignOne);

            assertFalse(dataSource.updateCampaign(campaignTwo));
        }

        @Test
        @DisplayName("returns false on a null campaign")
        void rejectNulLCampaign(){
            assertFalse(dataSource.updateCampaign(null));
        }
    }

    @Nested
    @DisplayName("getCampaigns")
    class GetCampaigns{
        @Test
        @DisplayName("returns an empty list when no campaigns exist")
        void returnsEmptyListInitially() {
            assertTrue(dataSource.getCampaigns().isEmpty());
        }

        @Test
        @DisplayName("returns the singular saved campaign")
        void returnsAddedCampaign() {
            Campaign campaign = utils.generateCampaign();

            dataSource.addCampaign(campaign);

            assertEquals(1, dataSource.getCampaigns().size());
            assertEquals(campaign, dataSource.getCampaigns().getFirst());
        }

        @Test
        @DisplayName("returns all saved campaigns")
        void returnsAllAddedCampaigns(){
            Campaign campaignOne = utils.generateCampaign();
            Campaign campaignTwo = utils.generateCampaign();
            Campaign campaignThree = utils.generateCampaign();

            dataSource.addCampaign(campaignOne);
            dataSource.addCampaign(campaignTwo);

            assertEquals(2, dataSource.getCampaigns().size());
            assertTrue(dataSource.getCampaigns().contains(campaignOne));
            assertTrue(dataSource.getCampaigns().contains(campaignTwo));

            dataSource.addCampaign(campaignThree);

            assertEquals(3, dataSource.getCampaigns().size());
            assertTrue(dataSource.getCampaigns().contains(campaignOne));
            assertTrue(dataSource.getCampaigns().contains(campaignTwo));
            assertTrue(dataSource.getCampaigns().contains(campaignThree));
        }
    }

    @Nested
    @DisplayName("loadCovenantsFromCampaign")
    class LoadCovenantsFromCampaign {
        @Test
        @DisplayName("returns the singular covenant associated with the campaign")
        void returnsSingularCovenant() {

        }

        @Test
        @DisplayName("returns the covenants associated with a campaign")
        void returnsMultipleCovenants(){

        }

        @Test
        @DisplayName("returns an empty list when no covenants exist for the campaign")
        void returnsEmptyListForNoCovenants() {
            Campaign campaign = utils.generateCampaign();
            dataSource.addCampaign(campaign);

            assertTrue(dataSource.loadCovenantsFromCampaign(campaign).isEmpty());
        }

        @Test
        @DisplayName("returns an empty list for a null campaign")
        void returnsEmptyListForNullCampaign() {
            assertTrue(dataSource.loadCovenantsFromCampaign(null).isEmpty());
        }
    }

}
