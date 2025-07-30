package application.commands;

import application.characters.Attribute;
import application.characters.Character;
import application.terminal.CharacterController;
import org.beryx.textio.TextIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
Steps in Character Creation:

1. Virtues & Flaws
2. Characteristics
3. Early Childhood
4. Later Life / Apprenticeship
4.5 Spells
5. Personality

Arts & Characteristics Formula: n(n+1)/2
 */
public class CharacterEditor extends CharacterController {

    public CharacterEditor(TextIO source, ArrayList<Character> arr) {
        super(source, arr);
    }

    @Override
    public boolean execute() {
        return false;
    }

    public List<Integer> getCharacteristics(String prompt){
        List<Integer> characteristics = new ArrayList<>();

        for(Attribute attribute : Attribute.values()){
            characteristics.add(super.getInt(String.format("%s: ", attribute)));
        }

        if(verifyCharacteristics(characteristics) == null){
            return characteristics;
        }else{
            //Print Options + Costs
            Attribute[] attributeList = Attribute.values();

            super.print("This array is not valid. Try again");
            for(int i = 0; i < attributeList.length; i++){
                super.print(String.format("%d. %s: %d", i, attributeList[i], characteristics.get(i)));
            }
            //Call getCharacteristics() again, but with a "do better" as the string prompt
                //Add a "if prompt is not null, then print, else continue"
            return getCharacteristics("\n");
        }
    }

    /**
    Method to verify if the given set of characteristics fits the requirements. As per RoP:I, the point values of
     characteristics are equivalent to that of arts. According to the base book the progression is that of arithmetic
     summation. Therefore, in order to calculate the cost, I will be using the summation formula

     @param characteristics is the list of characteristics that need to be checked

     @return list of respective costs if a mistake was made, or null if it goes through correctly
     */
    public static List<Integer> verifyCharacteristics(List<Integer> characteristics){
        int points = 7;
        List<Integer> costs = new ArrayList<>();

        for(int characteristicValue : characteristics){
            //Do with the absolute value in order to preserve the sign, if the given characteristic is negative
            int pointsValue = (Math.abs(characteristicValue) * (characteristicValue + 1)) / 2;
            costs.add(pointsValue);
            points += pointsValue;
        }

        if(points >= 0){
            return null;
        }
        return costs;
    }
}
