package tests.commands;

import application.data.*;
import application.models.Campaign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static tests.utils.generateCampaign;
import static tests.utils.generateCovenant;

public class ListSelectAndShowCovenantTests {
    IDataSource superSource;
    ICharacterDataSource dataSource;
    ICovenantDataSource covenantDataSource;
    ICampaignDataSource campaignDataSource;
    Campaign campaign;

    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        dataSource = new CharacterDataSource(superSource);
        covenantDataSource = new CovenantDataSource(superSource);
        campaignDataSource = new CampaignDataSource(superSource);

        campaign = generateCampaign();
        campaignDataSource.addCampaign(campaign);
    }

    @Nested
    class ListCovenants{
        @Test
        void listSingularCovenant(){

        }

        @Test
        void listMultipleCovenants(){
            fail();
        }
    }

    @Nested
    class SelectCovenant{
        @Test
        void selectOnZeroCovenants(){
            fail();
        }

        @Test
        void selectSingularCovenant(){
            fail();
        }

        @Test
        void selectFromMultipleCovenants(){
            fail();
        }
    }

    @Nested
    class ShowCovenant{
        @Test
        void showCovenantWithNoFeaturesNoCharacters(){
            fail();
        }

        @Test
        void showCovenantWithSingularFeatureNoCharacters(){
            fail();
        }

        @Test
        void showCovenantWithMultipleFeaturesNoCharacters(){
            fail();
        }

        @Test
        void showCovenantWithSingularCharacterNoFeatures(){
            fail();
        }

        @Test
        void showCovenantWithMultipleCharactersNoFeatures(){
            fail();
        }

        @Test
        void showCovenantWithSingularCharacterSingularFeature(){
            fail();
        }

        @Test
        void showCovenantWithMultipleCharactersMultipleFeatures(){
            fail();
        }
    }
}
