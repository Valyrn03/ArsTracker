package tests;

import application.characters.CharacterBase;
import application.utils.Abilities;
import application.utils.characterUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Character & Character Sheet Testing
public class CharacterTests {
    @Test
    void testCharacterAbilities(){
        CharacterBase character = new CharacterBase("Character One", 20, "grog");
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
        fail();
    }

    @Test
    void testDeserialize(){
        fail();
    }
}
