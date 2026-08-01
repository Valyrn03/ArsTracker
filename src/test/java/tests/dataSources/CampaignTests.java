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
}
