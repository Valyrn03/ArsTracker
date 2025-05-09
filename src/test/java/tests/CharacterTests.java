package tests;

import application.characters.Character;
import application.utils.Abilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Character & Character Sheet Testing
public class CharacterTests {
    @Test
    void testCharacterAbilities(){
        Character character = new Character("Character One", 20, "grog");
        character.improveAbility(Abilities.Ability.STEALTH, 15);
        character.improveAbility(Abilities.Ability.SINGLE_WEAPON, 10);

        assertEquals(2, character.getAbilityScore(Abilities.Ability.STEALTH));
        assertEquals(1, character.getAbilityScore(Abilities.Ability.SINGLE_WEAPON));
        assertEquals(0, character.getAbilityScore(Abilities.Ability.AWARENESS));

        assertEquals(15, character.getAbility(Abilities.Ability.STEALTH));
        assertEquals(10, character.getAbility(Abilities.Ability.SINGLE_WEAPON));
        assertEquals(0, character.getAbility(Abilities.Ability.AWARENESS));

        character.improveAbility(Abilities.Ability.STEALTH, 100);

        assertEquals(115, character.getAbility(Abilities.Ability.STEALTH));
        assertEquals(6, character.getAbilityScore(Abilities.Ability.STEALTH));
    }

    @Test
    void testSerialize(){
        Character sampleCharacter = new Character("Sample Character", 100, "companion");

        String serialized = "Sample Character\n100 companion\nVirtues\nFlaws\nAbilities\n";
        assertEquals(serialized, sampleCharacter.serialize());

        sampleCharacter.addFeature("Feature A", true);
        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\nFlaws\nAbilities\n";
        assertEquals(serialized, sampleCharacter.serialize());

        sampleCharacter.addFeature("Feature B", true);
        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\nAbilities\n";
        assertEquals(serialized, sampleCharacter.serialize());

        sampleCharacter.addFeature("Feature C", false);
        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\n\tFeature C\nAbilities\n";
        assertEquals(serialized, sampleCharacter.serialize());

        sampleCharacter.improveAbility(Abilities.Ability.ART_OF_MEMORY, 30);
        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\n\tFeature C\nAbilities\n\t30 ART_OF_MEMORY\n";
        assertEquals(serialized, sampleCharacter.serialize());

        sampleCharacter.improveAbility(Abilities.Ability.ATHLETICS, 82);
        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\n\tFeature C\nAbilities\n\t30 ART_OF_MEMORY\n\t82 ATHLETICS\n";
        assertEquals(serialized, sampleCharacter.serialize());
    }

    @Test
    void testDeserialize(){
        Character character = new Character("Character", 27, "magus");


    }
}
