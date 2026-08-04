package application.utils;

import application.models.enums.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static application.CharacterController.calculateCost;

@Slf4j
public class CharacterUtils{
    public CharacterUtils(){

    }

    public static int abilityExperienceToScore(int experience){
        Logger logger = Logger.getLogger(CharacterUtils.class.getName());
        logger.info("Experience: " + experience);
        int score = 0;

        while(experience > 0){
            experience -= (score + 1) * 5;
            score++;
            logger.info("Experience: " + experience + " with a score of " + score);
        }

        logger.info("Returning " + score);
        if(experience < 0){
            score--;
        }
        return score;
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
            int pointsValue = calculateCost(characteristicValue);
            if(characteristicValue > 0){
                pointsValue = pointsValue * -1;
            }
            costs.add(pointsValue);
            points += pointsValue;
        }

        int finalPoints = points;
        log.info("Array: {}\nCosts: {}\nPoints:{}", characteristics.toString(), costs.toString(), finalPoints);
        if(points >= 0){
            return null;
        }
        return costs;
    }

    public static String format(Object obj){
        return obj.toString().substring(0, 1).toUpperCase() + obj.toString().substring(1).toLowerCase();
    }
}
