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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CampaignTests {
    @BeforeEach
    void setUp(){
        IDataSource superSource = new MockDataSource();
        CampaignDataSource dataSource = new CampaignDataSource(superSource);
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

        }
    }
}
