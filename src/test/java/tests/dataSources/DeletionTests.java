package tests.dataSources;

import application.data.*;
import application.models.*;
import application.models.enums.Art;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static tests.utils.*;

public class DeletionTests {
    IDataSource dataSource;
    ICampaignDataSource campaignDataSource;
    ICovenantDataSource covenantDataSource;
    ICharacterDataSource characterDataSource;

    @BeforeEach
    void setUp(){
        dataSource = new MockDataSource();
        campaignDataSource = new CampaignDataSource(dataSource);
        covenantDataSource = new CovenantDataSource(dataSource);
        characterDataSource = new CharacterDataSource(dataSource);
    }

    @Nested
    class CampaignDeletion{
        @Test
        void deleteCampaignWithNoCovenants(){
            Campaign campaign = generateCampaign();
            campaignDataSource.addCampaign(campaign);

            assertTrue(dataSource.deleteCampaign(campaign));
            assertTrue(campaignDataSource.loadCampaignFromName(campaign.getName()).isEmpty());
        }

        @Test
        void deleteNonexistentCampaign(){
            Campaign campaign = generateCampaign();
            assertFalse(dataSource.deleteCampaign(campaign));
        }

        @Test
        void deleteNullCampaign(){
            assertFalse(dataSource.deleteCampaign(null));
        }

        @Test
        void deleteCampaignWithOneCovenant(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            campaign.addCovenant(covenant);

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            assertTrue(covenantDataSource.loadCovenantFromId(covenant.getId()).isPresent());

            assertTrue(dataSource.deleteCampaign(campaign));
            assertTrue(campaignDataSource.loadCampaignFromName(campaign.getName()).isEmpty());
            assertTrue(covenantDataSource.loadCovenantFromId(covenant.getId()).isEmpty());
        }

        @Test
        void deleteCampaignWithMultipleCovenants(){
            Campaign campaign = generateCampaign();
            Covenant covenantOne = generateCovenant();
            Covenant covenantTwo = generateCovenant();
            campaign.addCovenant(covenantOne);
            campaign.addCovenant(covenantTwo);

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenantOne, campaign);
            covenantDataSource.addCovenant(covenantTwo, campaign);
            assertTrue(covenantDataSource.loadCovenantFromId(covenantOne.getId()).isPresent());
            assertTrue(covenantDataSource.loadCovenantFromId(covenantTwo.getId()).isPresent());

            assertTrue(dataSource.deleteCampaign(campaign));
            assertTrue(campaignDataSource.loadCampaignFromName(campaign.getName()).isEmpty());
            assertTrue(covenantDataSource.loadCovenantFromId(covenantOne.getId()).isEmpty());
            assertTrue(covenantDataSource.loadCovenantFromId(covenantTwo.getId()).isEmpty());
        }
    }

    @Nested
    class CovenantDeletion{
        @Test
        void deleteNonexistentCovenant(){
            Covenant covenant = generateCovenant();
            assertTrue(dataSource.deleteCovenant(covenant));
        }

        @Test
        void deleteCovenantWithNoFeaturesCharactersOrVis(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);

            assertTrue(dataSource.deleteCovenant(covenant));
            assertTrue(covenantDataSource.loadCovenantFromId(covenant.getId()).isEmpty());
            assertEquals(Collections.emptyList(), covenantDataSource.loadCovenantIdsFromCampaign(campaign));
        }

        @Test
        void deleteCovenantWithOneFeature(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            CovenantFeature feature = generateCovenantFeature();

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            covenantDataSource.saveCovenantFeature(feature);
            covenantDataSource.addFeatureToCovenant(covenant, feature);

            assertTrue(dataSource.deleteCovenant(covenant));
            assertEquals(Collections.emptyList(), covenantDataSource.loadFeaturesFromCovenant(covenant));
        }

        @Test
        void deleteCovenantWithMultipleFeatures(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            List<CovenantFeature> features = List.of(new CovenantFeature[]{generateCovenantFeature(), generateCovenantFeature()});

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            for(CovenantFeature feature : features){
                covenantDataSource.saveCovenantFeature(feature);
                covenantDataSource.addFeatureToCovenant(covenant, feature);
            }

            assertTrue(dataSource.deleteCovenant(covenant));
            assertEquals(Collections.emptyList(), covenantDataSource.loadFeaturesFromCovenant(covenant));
        }

        @Test
        void deleteCovenantWithOneCharacter(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter();

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);

            assertTrue(dataSource.deleteCovenant(covenant));
            assertEquals(Collections.emptyList(), characterDataSource.loadCovenantCharacters(covenant));
            assertTrue(characterDataSource.loadBaseCharacterFromId(character.getId()).isEmpty());
        }

        @Test
        void deleteCovenantWithMultipleCharacters(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            List<ArsCharacter> characters = List.of(new ArsCharacter[]{generateCharacter(), generateCharacter()});

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            for(ArsCharacter character : characters){
                characterDataSource.addCharacterToCovenant(character, covenant);
            }

            assertTrue(dataSource.deleteCovenant(covenant));
            assertEquals(Collections.emptyList(), characterDataSource.loadCovenantCharacters(covenant));

            for(ArsCharacter character : characters){
                assertTrue(characterDataSource.loadBaseCharacterFromId(character.getId()).isEmpty());
            }
        }

        @Test
        void deleteCovenantWithVis(){
            fail();
        }

        @Test
        void deleteCovenantWithOneFeatureAndOneCharacter(){
            fail();
        }

        @Test
        void deleteCovenantWithOneFeatureAndVis(){
            fail();
        }

        @Test
        void deleteCovenantWithOneCharacterAndVis(){
            fail();
        }
    }

    @Nested
    class CharacterDeletion{
        @Test
        void deleteNonexistentCharacter(){
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);
            assertTrue(dataSource.deleteCharacter(character));
        }

        @Test
        void deleteCharacterWithNoFeaturesOrAbilities(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), covenant.getPlayerCharacters());
            assertEquals(Collections.emptyList(), characterDataSource.loadCovenantCharacters(covenant));
        }

        @Test
        void deleteCharacterWithOneFeature(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);
            CharacterFeature feature = generateCharacterFeature();

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);
            characterDataSource.saveNewFeature(feature);
            characterDataSource.addFeatureToCharacter(character, feature);

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), characterDataSource.loadFeaturesFromCharacter(character));

        }

        @Test
        void deleteCharacterWithMultipleFeatures(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);
            List<CharacterFeature> features = List.of(new CharacterFeature[]{generateCharacterFeature(), generateCharacterFeature()});

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);
            for(CharacterFeature feature : features){
                characterDataSource.saveNewFeature(feature);
                characterDataSource.addFeatureToCharacter(character, feature);
            }

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), characterDataSource.loadFeaturesFromCharacter(character));
        }

        @Test
        void deleteCharacterWithOneNoncategoricalAbility(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);
            Ability ability = generateAbility(false);
            character.addAbility(ability);

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);
            dataSource.addAbility(character.getId(), ability);

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), dataSource.loadAbilitiesById(character.getId()));
        }

        @Test
        void deleteCharacterWithCategoricalAbility(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);
            Ability ability = generateAbility(true);
            character.addAbility(ability);

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);
            dataSource.addAbility(character.getId(), ability);

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), dataSource.loadAbilitiesById(character.getId()));
        }

        @Test
        void deleteCharacterWithMultipleAbilities(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);
            List<Ability> abilities = List.of(new Ability[]{generateAbility(), generateAbility()});

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);

            for(Ability ability : abilities){
                character.addAbility(ability);
                dataSource.addAbility(character.getId(), ability);
            }

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), dataSource.loadAbilitiesById(character.getId()));
        }

        @Test
        void deleteCharacterWithFeatureAndAbility(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.COMPANION);
            CharacterFeature feature = generateCharacterFeature();
            Ability ability = generateAbility();

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);
            characterDataSource.saveNewFeature(feature);
            characterDataSource.addFeatureToCharacter(character, feature);
            dataSource.addAbility(character.getId(), ability);

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), characterDataSource.loadFeaturesFromCharacter(character));
        }

        @Test
        void deleteMagusWithNoFeaturesOrAbilities(){
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), covenant.getPlayerCharacters());
            assertEquals(Collections.emptyList(), characterDataSource.loadCovenantCharacters(covenant));
        }

        @Test
        void deleteMagusWithOneArt(){
            Random random = new Random();
            Campaign campaign = generateCampaign();
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            Art art = Art.values()[random.nextInt(Art.values().length)];
            character.incrementArt(art, random.nextInt(1000));

            campaignDataSource.addCampaign(campaign);
            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);
            characterDataSource.updateCharacterArts(character);

            assertTrue(dataSource.deleteCharacter(character));
            assertEquals(Collections.emptyList(), covenant.getPlayerCharacters());
            assertEquals(Collections.emptyList(), characterDataSource.loadCovenantCharacters(covenant));
            assertEquals(0, characterDataSource.loadCharacterArt(character, art));
        }
    }

    //Test out deleting a campaign that has a covenant that contains characters
    @Test
    void delete(){
        fail();
    }
}
