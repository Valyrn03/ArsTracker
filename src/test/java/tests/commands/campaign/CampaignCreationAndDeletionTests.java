package tests.commands.campaign;

import application.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

public class CampaignCreationAndDeletionTests {
    IDataSource superSource;
    ICampaignDataSource campaignDataSource;
    ICovenantDataSource covenantDataSource;
    ICharacterDataSource characterDataSource;

    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        campaignDataSource = new CampaignDataSource(superSource);
        covenantDataSource = new CovenantDataSource(superSource);
        characterDataSource = new CharacterDataSource(superSource);
    }

    @Nested
    class CampaignCreation{

    }

    @Nested
    class CampaignDeletion{

    }
}
