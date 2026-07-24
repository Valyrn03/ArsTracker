package tests.dataSources;

import application.data.CampaignDataSource;
import application.data.IDataSource;
import application.data.MockDataSource;
import application.models.Campaign;
import application.models.Covenant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tests.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static tests.utils.*;

@Slf4j
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
            Campaign campaign = generateCampaign();

            boolean result = dataSource.addCampaign(campaign.getName(), campaign.getCurrentSeason());

            assertTrue(result);
            assertEquals(1, dataSource.getCampaigns().size());
            assertTrue(dataSource.getCampaigns().contains(campaign));
        }

        @Test
        @DisplayName("should be returning false when a campaign with the same id or name already exists")
        void testAddCampaignSameName(){
            Campaign baseCampaign = generateCampaign();

            assertTrue(dataSource.addCampaign(baseCampaign.getName(), baseCampaign.getCurrentSeason()));

            assertFalse(dataSource.addCampaign(baseCampaign.getName(), baseCampaign.getCurrentSeason() + 1));
            assertEquals(1, dataSource.getCampaigns().size());
            assertEquals(baseCampaign, dataSource.getCampaigns().getFirst());
        }

        @Test
        void testNullCampaign(){
            assertFalse(dataSource.addCampaign(null, 0));
        }

        @Test
        void testAddCampaignNegativeAge(){
            UUID id = UUID.randomUUID();
            Random random = new Random();

            assertFalse(dataSource.addCampaign(id.toString(), random.nextInt(-10000, 0)));
        }
    }

    @Nested
    @DisplayName("loadCampaignFromName")
    class LoadCampaignFromName{
        @Test
        @DisplayName("Test with a singular known campaign")
        void returnsExistingCampaign(){
            Campaign campaign = generateCampaign();

            dataSource.addCampaign(campaign);

            Optional<Campaign> result = dataSource.loadCampaignFromName(campaign.getName());

            assertTrue(result.isPresent());
            assertEquals(campaign, result.get());
        }

        @Test
        @DisplayName("Correctly returns an empty object when the campaign does not exist")
        void returnsEmpty(){
            Optional<Campaign> result = dataSource.loadCampaignFromName(String.valueOf(UUID.randomUUID()));
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("updateCampaign")
    class UpdateCampaign{
        @Test
        @DisplayName("Updates an existing campaign and returns true")
        void updateExistingCampaign() throws CloneNotSupportedException {
            Campaign baseCampaign = generateCampaign();
            dataSource.addCampaign(baseCampaign);

            baseCampaign.advanceSeason();
            dataSource.updateCampaign(baseCampaign);

            Optional<Campaign> newCampaign = dataSource.loadCampaignFromName(baseCampaign.getName());
            assertTrue(newCampaign.isPresent());
            assertTrue(baseCampaign.equals(newCampaign.get()));
        }

        @Test
        @DisplayName("returns false on a nonexistent campaign with no saved campaigns")
        void rejectsUnknownCampaignWithNoSaved(){
            Campaign campaign = generateCampaign();
            assertFalse(dataSource.updateCampaign(campaign));
        }

        @Test
        @DisplayName("returns false on a nonexistent campaign")
        void rejectsUnknownCampaign(){
            Campaign campaignOne = generateCampaign();
            Campaign campaignTwo = generateCampaign();

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
            Campaign campaign = generateCampaign();

            dataSource.addCampaign(campaign);

            assertEquals(1, dataSource.getCampaigns().size());
            assertEquals(campaign, dataSource.getCampaigns().getFirst());
        }

        @Test
        @DisplayName("returns all saved campaigns")
        void returnsAllAddedCampaigns(){
            Campaign campaignOne = generateCampaign();
            Campaign campaignTwo = generateCampaign();
            Campaign campaignThree = generateCampaign();

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

            dataSource.addCampaign(campaign);
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

            dataSource.addCampaign(campaign);
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
            dataSource.addCampaign(campaign);

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

            dataSource.addCampaign(campaignOne);
            dataSource.addCampaign(campaignTwo);
            assertTrue(addCovenant(covenantOne, campaignOne.getName()));
            assertTrue(addCovenant(covenantTwo, campaignTwo.getName()));

            assertEquals(1, dataSource.loadCovenantIdsFromCampaign(campaignOne).size());
            assertEquals(covenantOne.getId(), dataSource.loadCovenantIdsFromCampaign(campaignOne).getFirst());
            assertEquals(1, dataSource.loadCovenantIdsFromCampaign(campaignTwo).size());
            assertEquals(covenantTwo.getId(), dataSource.loadCovenantIdsFromCampaign(campaignTwo).getFirst());
        }
    }

}
