package tests.dataSources;

import application.data.*;
import application.models.*;
import application.models.enums.Art;
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
            dataSource.addCharacterToCovenant(character, covenant);

            assertEquals(Optional.of(character), dataSource.loadBaseCharacterFromId(character.getId()));
        }

        @Test
        @DisplayName("returns correct character among multiple in the same covenant")
        void returnsSpecificCharacter(){
            ArsCharacter characterOne = generateCharacter();
            ArsCharacter characterTwo = generateCharacter();

            dataSource.addCharacterToCovenant(characterOne, covenant);
            dataSource.addCharacterToCovenant(characterTwo, covenant);

            assertEquals(Optional.of(characterOne), dataSource.loadBaseCharacterFromId(characterOne.getId()));
        }

        @Test
        @DisplayName("returns correct character across multiple covenants")
        void returnsOnMultipleCovenants(){
            Covenant covenantTwo = generateCovenant();
            covenantDataSource.addCovenant(covenantTwo, campaign);

            ArsCharacter character = generateCharacter();
            dataSource.addCharacterToCovenant(character, covenant);

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
            dataSource.addCharacterToCovenant(character, covenant);

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

            dataSource.addCharacterToCovenant(character, covenant);
            superSource.addAbility(character.getId(), ability);

            List<Ability> abilities = superSource.loadAbilitiesById(character.getId());

            assertEquals(1, abilities.size());
            assertTrue(abilities.contains(ability));
        }

        @Test
        void returnSingularCategoricalCharacterAbility(){
            ArsCharacter character = generateCharacter();
            Ability ability = generateAbility(true);

            dataSource.addCharacterToCovenant(character, covenant);
            superSource.addAbility(character.getId(), ability);

            assertEquals(1, superSource.loadAbilitiesById(character.getId()).size());
            assertEquals(ability, superSource.loadAbilitiesById(character.getId()).getFirst());
        }

        @Test
        @DisplayName("returns multiple abilities from a given character")
        void returnsMultipleAbilities(){
            ArsCharacter character = generateCharacter();
            Ability abilityOne = generateAbility();
            Ability abilityTwo = generateAbility();

            dataSource.addCharacterToCovenant(character, covenant);
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
            CharacterFeature feature = generateCharacterFeature();
            feature.addAbility(generateAbility(false));

            dataSource.saveNewFeature(feature);
            superSource.addAbility(feature.getId(), feature.getAbilities().getFirst());

            assertEquals(feature.getAbilities(), superSource.loadAbilitiesById(feature.getId()));
        }

        @Test
        void returnsSingularCategoricalFeatureAbility(){
            CharacterFeature feature = generateCharacterFeature();
            feature.addAbility(generateAbility(true));

            dataSource.saveNewFeature(feature);
            superSource.addAbility(feature.getId(), feature.getAbilities().getFirst());

            assertEquals(feature.getAbilities(), superSource.loadAbilitiesById(feature.getId()));
        }

        @Test
        @DisplayName("Given one ability that two characters have, return the correct one (difference would be XP and speciality)")
        void returnsOnSharedAbility(){
            Random random = new Random();
            Ability ability = generateAbility();
            Ability altAbility = new Ability(ability.getCategory(), ability.getAbility(), ability.getSpeciality(), random.nextInt(1000));

            ArsCharacter characterOne = generateCharacter();
            ArsCharacter characterTwo = generateCharacter();

            dataSource.addCharacterToCovenant(characterOne, covenant);
            characterOne.addAbility(ability);
            superSource.addAbility(characterOne.getId(), ability);

            dataSource.addCharacterToCovenant(characterTwo, covenant);
            characterTwo.addAbility(altAbility);
            superSource.addAbility(characterTwo.getId(), characterTwo.getAbilities().getFirst());

            assertEquals(characterOne.getAbilities(), superSource.loadAbilitiesById(characterOne.getId()));
            assertNotEquals(characterTwo.getAbilities(), superSource.loadAbilitiesById(characterOne.getId()));

            assertEquals(characterTwo.getAbilities(), superSource.loadAbilitiesById(characterTwo.getId()));
            assertNotEquals(characterOne.getAbilities(), superSource.loadAbilitiesById(characterTwo.getId()));
        }

        @Test
        void returnsTwoTypesOfCategoricalAbilities(){
            Ability ability = generateAbility(true);
            Ability altAbility = new Ability(ability.getCategory(), UUID.randomUUID().toString(), ability.getSpeciality(), ability.getExperience());

            ArsCharacter character = generateCharacter();
            character.addAbility(ability);
            character.addAbility(altAbility);

            dataSource.addCharacterToCovenant(character, covenant);
            superSource.addAbility(character.getId(), ability);
            superSource.addAbility(character.getId(), altAbility);

            assertEquals(2, superSource.loadAbilitiesById(character.getId()).size());
            assertTrue(superSource.loadAbilitiesById(character.getId()).contains(ability));
            assertTrue(superSource.loadAbilitiesById(character.getId()).contains(altAbility));
        }

        @Test
        void ignoresSubtypeOnNonCategorical(){
            Ability genericAbility = generateAbility(false);
            Ability fakeAbility = new Ability(genericAbility.getCategory(), UUID.randomUUID().toString(), genericAbility.getSpeciality(), genericAbility.getExperience());

            ArsCharacter character = generateCharacter();
            character.addAbility(genericAbility);

            dataSource.addCharacterToCovenant(character, covenant);
            superSource.addAbility(character.getId(), fakeAbility);
            assertEquals(fakeAbility, superSource.loadAbilitiesById(character.getId()).getFirst());
        }
    }

    @Nested
    class LoadFeaturesFromCharacter{
        @Test
        @DisplayName("returns a singular feature from a singular character")
        void returnsSingular(){
            ArsCharacter character = generateCharacter();
            CharacterFeature feature = generateCharacterFeature();

            dataSource.addCharacterToCovenant(character, covenant);
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

            dataSource.addCharacterToCovenant(character, covenant);
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
            assertTrue(dataSource.addCharacterToCovenant(character, covenant));
            log.info("New ID: {}", character.getId());
        }

        @Test
        void returnsFalseOnNonexistentCovenant(){
            assertFalse(dataSource.addCharacterToCovenant(generateCharacter(), generateCovenant()));
        }

        @Test
        @DisplayName("adding a character when there are multiple covenants")
        void addOnMultipleCovenants(){
            Covenant covenantTwo = generateCovenant();
            covenantDataSource.addCovenant(covenantTwo, campaign);

            ArsCharacter character = generateCharacter();

            assertTrue(dataSource.addCharacterToCovenant(character, covenant));
        }

        @Test
        void returnsFalseOnNullCovenant(){
            assertFalse(dataSource.addCharacterToCovenant(generateCharacter(), null));
        }

        @Test
        void returnsFalseOnNullCharacter(){
            assertFalse(dataSource.addCharacterToCovenant(null, covenant));
        }

        @Test
        @DisplayName("Make sure it fails if the covenant isn't loaded into the DB")
        void returnsFalseOnUnloadedCovenant(){
            Covenant unloadedCovenant = generateCovenant();

            assertFalse(dataSource.addCharacterToCovenant(generateCharacter(), unloadedCovenant));
        }
    }

    @Nested
    class AddAbilityToCharacter{
        @Test
        void returnsTrueOnAddition(){
            ArsCharacter character = generateCharacter();
            Ability ability = generateAbility();

            dataSource.addCharacterToCovenant(character, covenant);

            assertTrue(superSource.addAbility(character.getId(), ability));
        }

        @Test
        void returnsTrueOnMultipleAdditions(){
            ArsCharacter character = generateCharacter();
            Ability abilityOne = generateAbility();
            Ability abilityTwo = generateAbility();

            dataSource.addCharacterToCovenant(character, covenant);

            assertTrue(superSource.addAbility(character.getId(), abilityOne));
            assertTrue(superSource.addAbility(character.getId(), abilityTwo));
        }

        @Test
        void returnsFalseOnNullAbility(){
            ArsCharacter character = generateCharacter();
            dataSource.addCharacterToCovenant(character, covenant);

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

            dataSource.addCharacterToCovenant(character, covenant);
            dataSource.saveNewFeature(feature);

            assertTrue(dataSource.addFeatureToCharacter(character, feature));
        }

        @Test
        void returnsTrueOnMultipleAdditions(){
            ArsCharacter character = generateCharacter();
            CharacterFeature featureOne = generateCharacterFeature();
            CharacterFeature featureTwo = generateCharacterFeature();

            dataSource.addCharacterToCovenant(character, covenant);
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
            dataSource.addCharacterToCovenant(character, covenant);

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
            dataSource.addCharacterToCovenant(character, covenant);

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

    @Nested
    class LoadCovenantCharacters{
        ICharacterDataSource characterDataSource;
        @BeforeEach
        void setUpCDS(){
            characterDataSource = new CharacterDataSource(superSource);
        }

        @Test
        void returnsSingularCharacter(){
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter();

            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);

            assertEquals(1, dataSource.loadCovenantCharacters(covenant).size());
            assertEquals(character, dataSource.loadCovenantCharacters(covenant).getFirst());
        }

        @Test
        void returnsEmptyListOnNoCharacters(){
            Covenant covenant = generateCovenant();

            covenantDataSource.addCovenant(covenant, campaign);

            assertEquals(0, dataSource.loadCovenantCharacters(covenant).size());
            assertEquals(Collections.emptyList(), dataSource.loadCovenantCharacters(covenant));
        }

        @Test
        void returnsEmptyListOnUnloadedCovenant(){
            Covenant covenant = generateCovenant();
            ArsCharacter character = generateCharacter();

//            dataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(character, covenant);

            assertEquals(0, dataSource.loadCovenantCharacters(covenant).size());
            assertEquals(Collections.emptyList(), dataSource.loadCovenantCharacters(covenant));
        }

        @Test
        void returnsEmptyListOnNullCovenant(){
            assertEquals(0, dataSource.loadCovenantCharacters(null).size());
            assertEquals(Collections.emptyList(), dataSource.loadCovenantCharacters(null));
        }

        @Test
        void returnsMultipleCharacters(){
            Covenant covenant = generateCovenant();
            ArsCharacter characterOne = generateCharacter();
            ArsCharacter characterTwo = generateCharacter();

            covenantDataSource.addCovenant(covenant, campaign);
            characterDataSource.addCharacterToCovenant(characterOne, covenant);
            characterDataSource.addCharacterToCovenant(characterTwo, covenant);

            assertEquals(2, dataSource.loadCovenantCharacters(covenant).size());
            assertTrue(dataSource.loadCovenantCharacters(covenant).contains(characterOne));
            assertTrue(dataSource.loadCovenantCharacters(covenant).contains(characterTwo));
        }

        @Test
        void returnsCorrectCharacterIfMultipleCovenants(){
            Covenant covenantOne = generateCovenant();
            Covenant covenantTwo = generateCovenant();
            ArsCharacter character = generateCharacter();

            covenantDataSource.addCovenant(covenantOne, campaign);
            covenantDataSource.addCovenant(covenantTwo, campaign);
            characterDataSource.addCharacterToCovenant(character, covenantOne);

            assertEquals(1, dataSource.loadCovenantCharacters(covenantOne).size());
            assertEquals(character, dataSource.loadCovenantCharacters(covenantOne).getFirst());
            assertEquals(0, dataSource.loadCovenantCharacters(covenantTwo).size());
        }
    }

    @Nested
    class UpdateCharacterArts{
        @Test
        void returnsTrueOnSingularUpdate(){
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            dataSource.addCharacterToCovenant(character, covenant);

            Random random = new Random();
            character.incrementArt(Art.values()[random.nextInt(Art.values().length)], random.nextInt(1000));

            assertTrue(dataSource.updateCharacterArts(character));
        }

        @Test
        void returnsTrueOnMultipleUpdates(){
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            dataSource.addCharacterToCovenant(character, covenant);

            Random random = new Random();
            List<Art> arts = new ArrayList<>();

            for(int i = 0; i < 2; i++){
                arts.add(Art.values()[random.nextInt(Art.values().length)]);
            }

            while(arts.get(0).equals(arts.get(1))){
                arts.removeLast();
                arts.add(Art.values()[random.nextInt(Art.values().length)]);
            }

            for(Art art : arts){
                character.incrementArt(art, random.nextInt(1000));
            }

            assertTrue(dataSource.updateCharacterArts(character));
        }

        @Test
        void returnsFalseOnNonexistentCharacter(){
            assertFalse(dataSource.updateCharacterCharacteristics(generateCharacter()));
        }
    }

    @Nested
    class LoadCharacterArt{
        @Test
        void loadsSingularArt(){
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            Random random = new Random();
            Art art = Art.values()[random.nextInt(Art.values().length)];

            character.incrementArt(art, random.nextInt(1000));
            Map<Art, Integer> artMap = new HashMap<>(character.getArts());
            dataSource.addCharacterToCovenant(character, covenant);
            dataSource.updateCharacterArts(character); //Should refactor later to make this unnecessary

            character.setArt(art, 0);
            assertEquals(artMap.get(art), dataSource.loadCharacterArt(character, art));
            assertEquals(artMap.get(art), character.getArt(art));
        }

        @Test
        void loadsMultipleArts(){
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            Random random = new Random();

            List<Art> arts = new ArrayList<>();
            for (int i = 0; i < 2; i++){
                arts.add(Art.values()[random.nextInt(Art.values().length)]);
            }
            while (arts.get(0).equals(arts.get(1))){
                arts.removeLast();
                arts.add(Art.values()[random.nextInt(Art.values().length)]);
            }

            for(Art art : arts){
                character.incrementArt(art, random.nextInt(1000));
            }

            Map<Art, Integer> artMap = new HashMap<>(character.getArts());
            dataSource.addCharacterToCovenant(character, covenant);
            dataSource.updateCharacterArts(character); //Should refactor later to make this unnecessary

            for(Art art : arts){
                character.setArt(art, 0);
                assertEquals(artMap.get(art), dataSource.loadCharacterArt(character, art));
                assertEquals(artMap.get(art), character.getArt(art));
            }
            assertEquals(artMap, character.getArts());
        }

        @Test
        void failsOnNonexistentCharacter(){
            Random random = new Random();
            Art art = Art.values()[random.nextInt(Art.values().length)];
            assertEquals(-1, dataSource.loadCharacterArt(generateCharacter(), art));
        }

        @Test
        void returnsZeroOnArtIfNoArtSet(){
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            dataSource.addCharacterToCovenant(character, covenant);

            Random random = new Random();
            assertEquals(0, dataSource.loadCharacterArt(character, Art.values()[random.nextInt(Art.values().length)]));
        }

        //TODO: loadCharacterArts should update the character object
        @Test
        void returnsZeroOnArtIfOtherArtSet(){
            ArsCharacter character = generateCharacter(ArsCharacter.CharacterType.MAGUS);
            Random random = new Random();

            List<Art> arts = new ArrayList<>();
            for (int i = 0; i < 2; i++){
                arts.add(Art.values()[random.nextInt(Art.values().length)]);
            }
            while (arts.get(0).equals(arts.get(1))){
                arts.removeLast();
                arts.add(Art.values()[random.nextInt(Art.values().length)]);
            }

            character.incrementArt(arts.getFirst(), random.nextInt(1000));
            dataSource.addCharacterToCovenant(character, covenant);
            dataSource.updateCharacterArts(character); //Should refactor later to make this unnecessary

            assertEquals(0, dataSource.loadCharacterArt(character, arts.getLast()));
            assertNotEquals(0, dataSource.loadCharacterArt(character, arts.getFirst()));
        }
    }
}
