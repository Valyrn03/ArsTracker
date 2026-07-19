package tests.dataSources;

import application.data.*;
import application.models.*;
import application.models.enums.AbilityCategory;
import application.models.enums.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static tests.utils.*;

@Slf4j
public class CharacterTests {
    IDataSource superSource;
    ICharacterDataSource dataSource;
    ICovenantDataSource covenantDataSource;
    ICampaignDataSource campaignDataSource;
    Campaign campaign;
    Covenant covenant;

    @BeforeEach
    void setUp(){
        superSource = new MockDataSource();
        dataSource = new CharacterDataSource(superSource);
        covenantDataSource = new CovenantDataSource(superSource);
        campaignDataSource = new CampaignDataSource(superSource);

        campaign = generateCampaign();
        campaignDataSource.addCampaign(campaign);

        covenant = generateCovenant();
        covenantDataSource.addCovenant(covenant, campaign);
    }

    @Nested
    class loadBaseCharacterFromId{
        @Test
        @DisplayName("returns singular saved character")
        void returnsSingularCharacter(){
            ArsCharacter character = generateCharacter();
            dataSource.addBaseCharacterToCovenant(covenant, character);

            assertEquals(Optional.of(character), dataSource.loadBaseCharacterFromId(character.getId()));
        }

        @Test
        @DisplayName("returns correct character among multiple in the same covenant")
        void returnsSpecificCharacter(){
            ArsCharacter characterOne = generateCharacter();
            ArsCharacter characterTwo = generateCharacter();

            dataSource.addBaseCharacterToCovenant(covenant, characterOne);
            dataSource.addBaseCharacterToCovenant(covenant, characterTwo);

            assertEquals(Optional.of(characterOne), dataSource.loadBaseCharacterFromId(characterOne.getId()));
        }

        @Test
        @DisplayName("returns correct character across multiple covenants")
        void returnsOnMultipleCovenants(){
            Covenant covenantTwo = generateCovenant();
            covenantDataSource.addCovenant(covenantTwo, campaign);

            ArsCharacter character = generateCharacter();
            dataSource.addBaseCharacterToCovenant(covenant, character);

            assertEquals(Optional.of(character), dataSource.loadBaseCharacterFromId(character.getId()));
        }

        @Test
        void returnsEmptyOnNonexistentCharacter(){
            ArsCharacter character = generateCharacter();
            assertEquals(Optional.empty(), dataSource.loadBaseCharacterFromId(character.getId()));
        }
    }

    @Nested
    class UpdateCharacterCharacteristics{
        @Test
        void returnsTrueOnUpdate(){
            ArsCharacter character = generateCharacter();
            dataSource.addBaseCharacterToCovenant(covenant, character);

            Random random = new Random();
            Map<Attribute, Integer> newAttributes = new HashMap<>();
            for(Attribute ignored : Attribute.values()){
                newAttributes.put(ignored, random.nextInt(0, 6));
            }
            character.setAttributes(newAttributes);

            assertTrue(dataSource.updateCharacterCharacteristics(character));

            Optional<ArsCharacter> queried = dataSource.loadBaseCharacterFromId(character.getId());
            assertTrue(queried.isPresent());
            for(Attribute attribute : Attribute.values()){
                assertEquals(character.getAttribute(attribute), queried.get().getAttribute(attribute));
            }
        }

        @Test
        void returnsFalseOnNullCharacter(){
            assertFalse(dataSource.updateCharacterCharacteristics(null));
        }

        @Test
        void returnsFalseOnNonexistentCharacter(){
            assertFalse(dataSource.updateCharacterCharacteristics(generateCharacter()));
        }
    }

    @Nested
    class LoadAbilitiesFromId{
        @Test
        @DisplayName("returns a singular ability from a singular character")
        void returnSingularGeneralCharacterAbility(){
            ArsCharacter character = generateCharacter();
            Ability ability = generateAbility(false);

            dataSource.addBaseCharacterToCovenant(covenant, character);
            superSource.addAbility(character.getId(), ability);

            List<Ability> abilities = superSource.loadAbilitiesById(character.getId());

            assertEquals(1, abilities.size());
            assertTrue(abilities.contains(ability));
        }

        @Test
        void returnSingularCategoricalCharacterAbility(){
            ArsCharacter character = generateCharacter();
            Ability ability = generateAbility();


        }

        @Test
        @DisplayName("returns multiple abilities from a given character")
        void returnsMultipleAbilities(){
            ArsCharacter character = generateCharacter();
            Ability abilityOne = generateAbility();
            Ability abilityTwo = generateAbility();

            dataSource.addBaseCharacterToCovenant(covenant, character);
            superSource.addAbility(character.getId(), abilityOne);
            superSource.addAbility(character.getId(), abilityTwo);

            List<Ability> abilities = superSource.loadAbilitiesById(character.getId());

            assertEquals(2, abilities.size());
            assertTrue(abilities.contains(abilityOne));
            assertTrue(abilities.contains(abilityTwo));
        }

        @Test
        @DisplayName("returns an empty list on a nonexistent character")
        void returnsEmptyOnNonexistent(){
            assertTrue(superSource.loadAbilitiesById(generateCharacter().getId()).isEmpty());
        }

        @Test
        void returnsSingularGeneralFeatureAbility(){

        }

        @Test
        void returnsSingularCategoricalFeatureAbility(){

        }

        @Test
        @DisplayName("Given one ability that two characters have, return the correct one (difference would be XP and speciality)")
        void returnsOnSharedAbility(){

        }

        @Test
        void returnsTwoTypesOfCategoricalAbilities(){

        }

        @Test
        void ignoresSubtypeOnNonCategorical(){

        }
    }

    @Nested
    class LoadFeaturesFromCharacter{
        @Test
        @DisplayName("returns a singular feature from a singular character")
        void returnsSingular(){
            ArsCharacter character = generateCharacter();
            CharacterFeature feature = generateCharacterFeature();

            dataSource.addBaseCharacterToCovenant(covenant, character);
            dataSource.saveNewFeature(feature);
            dataSource.addFeatureToCharacter(character, feature);

            List<CharacterFeature> features = dataSource.loadFeaturesFromCharacter(character);

            assertEquals(1, features.size());
            assertTrue(features.contains(feature));
        }

        @Test
        @DisplayName("returns multiple features from a given character")
        void returnsMultipleFeatures(){
            ArsCharacter character = generateCharacter();
            CharacterFeature featureOne = generateCharacterFeature();
            CharacterFeature featureTwo = generateCharacterFeature();

            dataSource.addBaseCharacterToCovenant(covenant, character);
            dataSource.saveNewFeature(featureOne);
            dataSource.saveNewFeature(featureTwo);
            dataSource.addFeatureToCharacter(character, featureOne);
            dataSource.addFeatureToCharacter(character, featureTwo);

            List<CharacterFeature> features = dataSource.loadFeaturesFromCharacter(character);

            assertEquals(2, features.size());
            assertTrue(features.contains(featureOne));
            assertTrue(features.contains(featureTwo));
        }

        @Test
        @DisplayName("returns empty list on null character")
        void returnsEmptyOnNull(){
            assertTrue(dataSource.loadFeaturesFromCharacter(null).isEmpty());
        }

        @Test
        @DisplayName("returns an empty list on a nonexistent character")
        void returnsEmptyOnNonexistent(){
            assertTrue(dataSource.loadFeaturesFromCharacter(generateCharacter()).isEmpty());
        }
    }

    @Nested
    class LoadFeatureFromId{
        @Test
        void returnsBasicFeatureOnQuery(){
            CharacterFeature feature = generateCharacterFeature();

            dataSource.saveNewFeature(feature);

            Optional<CharacterFeature> loadedFeature = dataSource.loadFeatureFromId(feature.getId());
            assertEquals(Optional.of(feature), loadedFeature,
                    () -> Optional.of(feature).toString() + "\nvs\n" + loadedFeature.map(CharacterFeature::toString));
        }

        @Test
        void returnsEmptyOnNonexistent(){
            CharacterFeature feature = generateCharacterFeature();

            assertEquals(Optional.empty(), dataSource.loadFeatureFromId(feature.getId()));
        }

        //TODO already using a connection to an actual ability...
        @Test
        void returnsAbilityFeature(){
            CharacterFeature feature = generateCharacterFeature();
            feature.addAbility(generateAbility());

            dataSource.saveNewFeature(feature);

            Optional<CharacterFeature> loadedFeature = dataSource.loadFeatureFromId(feature.getId());
            assertEquals(Optional.of(feature), loadedFeature,
                    () -> Optional.of(feature).toString() + "\nvs\n" + loadedFeature.map(CharacterFeature::toString));
        }

        @Test
        void returnsMultipleAbilityFeature(){
            CharacterFeature feature = generateCharacterFeature();

            for(int i = 0; i < 2; i++){
                feature.addAbility(generateAbility());
            }

            dataSource.saveNewFeature(feature);

            Optional<CharacterFeature> loadedFeature = dataSource.loadFeatureFromId(feature.getId());
            assertEquals(Optional.of(feature), loadedFeature,
                    () -> Optional.of(feature).toString() + "\nvs\n" + loadedFeature.map(CharacterFeature::toString));
        }

        @Test
        void returnsSingularRuleFeature(){
            CharacterFeature feature = generateCharacterFeature();
            feature.addRule(UUID.randomUUID().toString());

            dataSource.saveNewFeature(feature);

            Optional<CharacterFeature> loadedFeature = dataSource.loadFeatureFromId(feature.getId());
            assertEquals(Optional.of(feature), loadedFeature,
                    () -> Optional.of(feature).toString() + "\nvs\n" + loadedFeature.map(CharacterFeature::toString));
        }

        @Test
        void returnsMultipleRuleFeature(){
            CharacterFeature feature = generateCharacterFeature();

            for(int i = 0; i < 2; i++){
                feature.addRule(UUID.randomUUID().toString());
            }

            dataSource.saveNewFeature(feature);

            Optional<CharacterFeature> loadedFeature = dataSource.loadFeatureFromId(feature.getId());
            assertEquals(Optional.of(feature), loadedFeature,
                    () -> Optional.of(feature).toString() + "\nvs\n" + loadedFeature.map(CharacterFeature::toString));
        }

        @Test
        void returnsAbilityAndRuleFeature(){
            CharacterFeature feature = generateCharacterFeature();
            feature.addAbility(generateAbility());
            feature.addRule(UUID.randomUUID().toString());

            dataSource.saveNewFeature(feature);

            Optional<CharacterFeature> loadedFeature = dataSource.loadFeatureFromId(feature.getId());
            assertEquals(Optional.of(feature), loadedFeature,
                    () -> Optional.of(feature).toString() + "\nvs\n" + loadedFeature.map(CharacterFeature::toString));
        }
    }

    @Nested
    class AddBaseCharacterToCovenant{
        @Test
        void returnsTrueOnAddition(){
            ArsCharacter character = generateCharacter();
            assertTrue(dataSource.addBaseCharacterToCovenant(covenant, character));
            log.info("New ID: {}", character.getId());
        }

        @Test
        void returnsFalseOnNonexistentCovenant(){
            assertFalse(dataSource.addBaseCharacterToCovenant(generateCovenant(), generateCharacter()));
        }

        @Test
        @DisplayName("adding a character when there are multiple covenants")
        void addOnMultipleCovenants(){
            Covenant covenantTwo = generateCovenant();
            covenantDataSource.addCovenant(covenantTwo, campaign);

            ArsCharacter character = generateCharacter();

            assertTrue(dataSource.addBaseCharacterToCovenant(covenant, character));
        }

        @Test
        void returnsFalseOnNullCovenant(){
            assertFalse(dataSource.addBaseCharacterToCovenant(null, generateCharacter()));
        }

        @Test
        void returnsFalseOnNullCharacter(){
            assertFalse(dataSource.addBaseCharacterToCovenant(covenant, null));
        }

        @Test
        @DisplayName("Make sure it fails if the covenant isn't loaded into the DB")
        void returnsFalseOnUnloadedCovenant(){
            Covenant unloadedCovenant = generateCovenant();

            assertFalse(dataSource.addBaseCharacterToCovenant(unloadedCovenant, generateCharacter()));
        }
    }

    @Nested
    class AddAbilityToCharacter{
        @Test
        void returnsTrueOnAddition(){
            ArsCharacter character = generateCharacter();
            Ability ability = generateAbility();

            dataSource.addBaseCharacterToCovenant(covenant, character);

            assertTrue(superSource.addAbility(character.getId(), ability));
        }

        @Test
        void returnsTrueOnMultipleAdditions(){
            ArsCharacter character = generateCharacter();
            Ability abilityOne = generateAbility();
            Ability abilityTwo = generateAbility();

            dataSource.addBaseCharacterToCovenant(covenant, character);

            assertTrue(superSource.addAbility(character.getId(), abilityOne));
            assertTrue(superSource.addAbility(character.getId(), abilityTwo));
        }

        @Test
        void returnsFalseOnNullAbility(){
            ArsCharacter character = generateCharacter();
            dataSource.addBaseCharacterToCovenant(covenant, character);

            assertFalse(superSource.addAbility(character.getId(), null));
        }

        @Test
        void returnsFalseOnNonexistentCharacter(){
            assertFalse(superSource.addAbility(generateCharacter().getId(), generateAbility()));
        }
    }

    @Nested
    class AddFeatureToCharacter{
        @Test
        void returnsTrueOnAddition(){
            ArsCharacter character = generateCharacter();
            CharacterFeature feature = generateCharacterFeature();

            dataSource.addBaseCharacterToCovenant(covenant, character);
            dataSource.saveNewFeature(feature);

            assertTrue(dataSource.addFeatureToCharacter(character, feature));
        }

        @Test
        void returnsTrueOnMultipleAdditions(){
            ArsCharacter character = generateCharacter();
            CharacterFeature featureOne = generateCharacterFeature();
            CharacterFeature featureTwo = generateCharacterFeature();

            dataSource.addBaseCharacterToCovenant(covenant, character);
            dataSource.saveNewFeature(featureOne);
            dataSource.saveNewFeature(featureTwo);

            assertTrue(dataSource.addFeatureToCharacter(character, featureOne));
            assertTrue(dataSource.addFeatureToCharacter(character, featureTwo));
        }

        @Test
        void returnsFalseOnNullCharacter(){
            assertFalse(dataSource.addFeatureToCharacter(null, generateCharacterFeature()));
        }

        @Test
        void returnsFalseOnNullFeature(){
            ArsCharacter character = generateCharacter();
            dataSource.addBaseCharacterToCovenant(covenant, character);

            assertFalse(dataSource.addFeatureToCharacter(character, null));
        }

        @Test
        void returnsFalseOnNonexistentCharacter(){
            assertFalse(dataSource.addFeatureToCharacter(generateCharacter(), generateCharacterFeature()));
        }

        @Test
        @DisplayName("Make sure it fails if the feature hasn't been saved yet")
        void returnsFalseOnUnsavedFeature(){
            ArsCharacter character = generateCharacter();
            dataSource.addBaseCharacterToCovenant(covenant, character);

            CharacterFeature unsavedFeature = generateCharacterFeature();

            assertFalse(dataSource.addFeatureToCharacter(character, unsavedFeature));
        }
    }

    @Nested
    class SaveNewFeature{
        @Test
        void returnsTrueOnAdditionOfBasicFeature(){
            CharacterFeature feature = generateCharacterFeature();
            assertTrue(dataSource.saveNewFeature(feature));
            assertNotEquals(0, feature.getId());
        }

        @Test
        void returnsFalseOnNullFeature(){
            assertFalse(dataSource.saveNewFeature(null));
        }

        @Test
        void returnsTrueOnSingularAbilityFeature(){
            CharacterFeature feature = generateCharacterFeature();
            Ability ability = generateAbility();

            feature.addAbility(ability);

            assertTrue(dataSource.saveNewFeature(feature));
        }

        @Test
        void returnsTrueOnMultipleAbilityFeature(){
            CharacterFeature feature = generateCharacterFeature();

            for(int i = 0; i < 2; i++){
                feature.addAbility(generateAbility());
            }

            assertTrue(dataSource.saveNewFeature(feature));
        }

        @Test
        void returnsTrueOnSingularRuleFeature(){
            CharacterFeature feature = generateCharacterFeature();
            feature.addRule(UUID.randomUUID().toString());

            assertTrue(dataSource.saveNewFeature(feature));
        }

        @Test
        void returnsTrueOnMultipleRuleFeature(){
            CharacterFeature feature = generateCharacterFeature();

            for(int i = 0; i < 2; i++){
                feature.addRule(UUID.randomUUID().toString());
            }

            assertTrue(dataSource.saveNewFeature(feature));
        }

        @Test
        void returnsTrueOnAbilityAndRuleFeature(){
            CharacterFeature feature = generateCharacterFeature();

            feature.addAbility(generateAbility());
            feature.addRule(UUID.randomUUID().toString());

            assertTrue(dataSource.saveNewFeature(feature));
        }
    }
}
