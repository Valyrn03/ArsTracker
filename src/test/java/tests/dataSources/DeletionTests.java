package tests.dataSources;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class DeletionTests {
    @Nested
    class CampaignDeletion{
        @Test
        void deleteCampaignWithNoCovenants(){
            fail();
        }

        @Test
        void deleteNonexistentCampaign(){
            fail();
        }

        @Test
        void deleteCampaignWithOneCovenant(){
            fail();
        }

        @Test
        void deleteCampaignWithMultipleCovenants(){
            fail();
        }
    }

    @Nested
    class CovenantDeletion{
        @Test
        void deleteNonexistentCovenant(){

        }

        @Test
        void deleteCovenantWithNoFeaturesCharactersOrVis(){

        }

        @Test
        void deleteCovenantWithOneFeature(){

        }

        @Test
        void deleteCovenantWithMultipleFeatures(){

        }

        @Test
        void deleteCovenantWithOneCharacter(){

        }

        @Test
        void deleteCovenantWithMultipleCharacters(){

        }

        @Test
        void deleteCovenantWithVis(){

        }

        @Test
        void deleteCovenantWithOneFeatureAndOneCharacter(){

        }

        @Test
        void deleteCovenantWithOneFeatureAndVis(){

        }

        @Test
        void deleteCovenantWithOneCharacterAndVis(){

        }
    }

    @Nested
    class CharacterDeletion{
        @Test
        void deleteNonexistentCharacter(){

        }

        @Test
        void deleteCharacterWithNoFeaturesOrAbilities(){

        }

        @Test
        void deleteCharacterWithOneFeature(){

        }

        @Test
        void deleteCharacterWithMultipleFeatures(){

        }

        @Test
        void deleteCharacterWithOneNoncategoricalAbility(){

        }

        @Test
        void deleteCharacterWithCategoricalAbility(){

        }

        @Test
        void deleteCharacterWithMultipleAbilities(){

        }

        @Test
        void deleteCharacterWithFeatureAndAbility(){

        }

        @Test
        void deleteMagusWithNoFeaturesOrAbilities(){

        }

        @Test
        void deleteMagusWithOneFeature(){

        }

        @Test
        void deleteMagusWithOneAbility(){

        }

        @Test
        void deleteMagusWithFeaturesAndAbilities(){

        }
    }

    //Test out deleting a campaign that has a covenant that contains characters
    @Test
    void delete(){
        fail();
    }
}
