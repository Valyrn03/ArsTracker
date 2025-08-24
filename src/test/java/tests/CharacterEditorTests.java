package tests;

import application.commands.CharacterEditor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterEditorTests {

    /*
    Test to confirm that a given characteristic array before the application of Virtues & Flaws is valid

    In order for the array to be valid, the point value of all characteristics must sum up to 7 or less
     */
    @Test
    void testValidCharacteristicVerifier(){
        //Total Remaining: 7
        List<Integer> characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{0, 0, 0, 0, 0, 0, 0, 0}));
        assertNull(CharacterEditor.verifyCharacteristics(characteristicArray));

        //Total Remaining: 6
        characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{-3, 2, -2, 0, -1, 3, 1, 1}));
        assertNull(CharacterEditor.verifyCharacteristics(characteristicArray));

        //Total Remaining: 0
        characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{0, 2, -2, 0, -1, 3, 1, 1}));
        assertNull(CharacterEditor.verifyCharacteristics(characteristicArray));

        //Total Remaining: 1
        characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{-3, 2, -2, 0, -1, 3, 3, 1}));
        assertNull(CharacterEditor.verifyCharacteristics(characteristicArray));

        //Total Remaining: 0
        characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{-3, 2, -2, 1, -1, 3, 3, 1}));
        assertNull(CharacterEditor.verifyCharacteristics(characteristicArray));

        //Total Remaining:
        characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{-3, 2, -2, 1, -1, 3, 3, 1}));
        assertNull(CharacterEditor.verifyCharacteristics(characteristicArray));
    }

    /*
    Tests characteristic configurations that are invalid due to attempting to take too high of values
     */
    @Test
    void testInvalidCharacteristicVerifier(){
        //Total Remaining: -17
        List<Integer> characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{2, 2, 2, 2, 2, 2, 2, 2}));
        assertNotNull(CharacterEditor.verifyCharacteristics(characteristicArray));

        //Total Remaining: -2
        characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{-1, 2, 1, -3, 3, 2, 2, 0}));
        assertNotNull(CharacterEditor.verifyCharacteristics(characteristicArray));

        //Total Remaining: -1
        characteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{-3, 2, -2, 1, 1, 3, 2, 2}));
        assertNotNull(CharacterEditor.verifyCharacteristics(characteristicArray));
    }

    /*
    Should be easy, but rather be safe than sorry
     */
    @Test
    void testIsCategorical(){
        fail();
    }

    /*
    Create arraylists of random objects, and make sure that the return is correct
     */
    @Test
    void testListToSql(){
        fail();
    }

    @Test
    void testGetCharacterCategories(){
        fail();
    }
}
