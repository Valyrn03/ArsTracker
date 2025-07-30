package tests;

import application.commands.CharacterEditor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

public class CharacterEditorTests {

    /*
    Test to confirm that a given characteristic array before the application of Virtues & Flaws is valid

    In order for the array to be valid, the point value of all characteristics must sum up to 7 or less
     */
    @Test
    void testCharacteristicVerifier(){
        List<Integer> defaultCharacteristicArray = new ArrayList<>(Arrays.asList(new Integer[]{0, 0, 0, 0, 0, 0, 0, 0}));

        assertNull(CharacterEditor.verifyCharacteristics(defaultCharacteristicArray));
    }
}
