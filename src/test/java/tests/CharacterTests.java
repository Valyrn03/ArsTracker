package tests;

import application.characters.Character;
import application.utils.Abilities;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Character & Character Sheet Testing
public class CharacterTests {
//    @Test
//    void testCharacterAbilities(){
//        Character character = new Character("Character One", 20, "grog");
//        character.improveAbility(Abilities.Ability.STEALTH, 15);
//        character.improveAbility(Abilities.Ability.SINGLE_WEAPON, 10);
//
//        assertEquals(2, character.getAbilityScore(Abilities.Ability.STEALTH));
//        assertEquals(1, character.getAbilityScore(Abilities.Ability.SINGLE_WEAPON));
//        assertEquals(0, character.getAbilityScore(Abilities.Ability.AWARENESS));
//
//        assertEquals(15, character.getAbility(Abilities.Ability.STEALTH));
//        assertEquals(10, character.getAbility(Abilities.Ability.SINGLE_WEAPON));
//        assertEquals(0, character.getAbility(Abilities.Ability.AWARENESS));
//
//        character.improveAbility(Abilities.Ability.STEALTH, 100);
//
//        assertEquals(115, character.getAbility(Abilities.Ability.STEALTH));
//        assertEquals(6, character.getAbilityScore(Abilities.Ability.STEALTH));
//    }
//
//    @Test
//    void testSerialize(){
//        Character sampleCharacter = new Character("Sample Character", 100, "companion");
//
//        String serialized = "Sample Character\n100 companion\nVirtues\nFlaws\nAbilities\n";
//        assertEquals(serialized, sampleCharacter.serialize());
//
//        sampleCharacter.addFeature("Feature A", true);
//        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\nFlaws\nAbilities\n";
//        assertEquals(serialized, sampleCharacter.serialize());
//
//        sampleCharacter.addFeature("Feature B", true);
//        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\nAbilities\n";
//        assertEquals(serialized, sampleCharacter.serialize());
//
//        sampleCharacter.addFeature("Feature C", false);
//        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\n\tFeature C\nAbilities\n";
//        assertEquals(serialized, sampleCharacter.serialize());
//
//        sampleCharacter.improveAbility(Abilities.Ability.ART_OF_MEMORY, 30);
//        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\n\tFeature C\nAbilities\n\t30 ART_OF_MEMORY\n";
//        assertEquals(serialized, sampleCharacter.serialize());
//
//        sampleCharacter.improveAbility(Abilities.Ability.ATHLETICS, 82);
//        serialized = "Sample Character\n100 companion\nVirtues\n\tFeature A\n\tFeature B\nFlaws\n\tFeature C\nAbilities\n\t30 ART_OF_MEMORY\n\t82 ATHLETICS\n";
//        assertEquals(serialized, sampleCharacter.serialize());
//    }
//
//    //REMEMBER TO TEST FLAWS ONLY
//        //Also, abilities before features
//    @Test
//    void testDeserialize(){
//        Character character = new Character("Character", 27, "magus");
//        assertEquals(0, character.compareTo(Character.deserialize(derserializedCharacter(0))), "Expected: \n" + character.serialize() + "Actual: \n" + Character.deserialize(derserializedCharacter(0)).serialize());
//
//        //Testing Characteristics Section
//        ArrayList<Integer> characteristics = new ArrayList<>();
//        for(int i = 0; i < 7; i++){
//            characteristics.add(i);
//        }
//        characteristics.add(-2);
//        character.setAttributes(characteristics);
//        assertEquals(0, character.compareTo(Character.deserialize(derserializedCharacter(1))), "Expected: \n" + character.serialize() + "Actual: \n" + Character.deserialize(derserializedCharacter(1)).serialize());
//
//        //Testing Virtues Only
//        character.addFeature("One", true);
//        character.addFeature("Two", true);
//        character.addFeature("Three", true);
//        character.addFeature("Four", true);
//        assertEquals(0, character.compareTo(Character.deserialize(derserializedCharacter(2))), "Expected: \n" + character.serialize() + "Actual: \n" + Character.deserialize(derserializedCharacter(2)).serialize());
//
//        //Testing Both Virtues & Flaws
//        character.addFeature("Eno", false);
//        character.addFeature("Owt", false);
//        character.addFeature("Eerht", false);
//        character.addFeature("Ruof", false);
//        assertEquals(0, character.compareTo(Character.deserialize(derserializedCharacter(3))), "Expected: \n" + character.serialize() + "Actual: \n" + Character.deserialize(derserializedCharacter(3)).serialize());
//
//        character.improveAbility(Abilities.Ability.STEALTH, 50);
//        character.improveAbility(Abilities.Ability.CAROUSE, 80);
//        character.improveAbility(Abilities.Ability.ATHLETICS, 30);
//        assertEquals(0, character.compareTo(Character.deserialize(derserializedCharacter(4))), "Expected: \n" + character.serialize() + "Actual: \n" + Character.deserialize(derserializedCharacter(-4)).serialize());
//
//        Character character2 = new Character("Character", 27, "magus");
//        character2.addFeature("Faw", false);
//        assertEquals(0, character2.compareTo(Character.deserialize(derserializedCharacter(-1))), "Expected: \n" + character2.serialize() + "Actual: \n" + Character.deserialize(derserializedCharacter(-1)).serialize());
//
//        Character character3 = new Character("Character", 27, "magus");
//        character3.improveAbility(Abilities.Ability.BARGAIN, 40);
//        assertEquals(0, character3.compareTo(Character.deserialize(derserializedCharacter(-2))), "Expected: \n" + character3.serialize() + "Actual: \n" + Character.deserialize(derserializedCharacter(-2)).serialize());
//    }
//
//    private ArrayList<String> derserializedCharacter(int index){
//        ArrayList<String> character = new ArrayList<>();
//        character.add("Character");
//        character.add("27 magus");
//        if(index > 0){
//            character.add("Int 0 Per 1 Str 2 Sta 3 Pre 4 Com 5 Dex 6 Qik -2");
//        }else{
//            character.add("Int 0 Per 0 Str 0 Sta 0 Pre 0 Com 0 Dex 0 Qik 0");
//        }
//        character.add("Virtues");
//        if(index > 1){
//            character.add("\tOne");
//            character.add("\tTwo");
//            character.add("\tThree");
//            character.add("\tFour");
//        }
//        character.add("Flaws");
//        if(index > 2){
//            character.add("\tEno");
//            character.add("\tOwt");
//            character.add("\tEerht");
//            character.add("\tRuof");
//        }
//        if(index == -1){
//            character.add("\tFaw");
//        }
//        character.add("Abilities");
//        if(index > 3){
//            character.add("30 ATHLETICS");
//            character.add("80 CAROUSE");
//            character.add("50 STEALTH");
//        }
//        if(index == -2){
//            character.add("40 BARGAIN");
//        }
//
//        return character;
//    }
}
