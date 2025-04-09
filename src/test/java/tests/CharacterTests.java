package tests;

import application.characters.CharacterBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Character & Character Sheet Testing
public class CharacterTests {
    @Test
    void testCharacterAbilities(){
        CharacterBase character = new CharacterBase("Character", "One", 20, false);
//        character.improveAbility("AbilityOne", 15);
//        character.improveAbility("AbilityTwo", 10);
//
//        assertEquals(2, character.getAbilityScore("AbilityOne"));
//        assertEquals(1, character.getAbilityScore("AbilityTwo"));
//        assertEquals(0, character.getAbilityScore("AbilityNull"));
//
//        assertEquals(15, character.getAbility("AbilityOne"));
//        assertEquals(10, character.getAbility("AbilityTwo"));
//        assertEquals(0, character.getAbility("AbilityNull"));
//
//        character.improveAbility("AbilityOne", 100);
//
//        assertEquals(115, character.getAbility("AbilityOne"));
//        assertEquals(6, character.getAbilityScore("AbilityOne"));
    }
}
