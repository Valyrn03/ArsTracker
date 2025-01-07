package application.utils;

import java.util.ArrayList;
import java.util.logging.Logger;

public class characterUtils {
    public static ArrayList<String> attributeList(){
        ArrayList<String> attributes = new ArrayList<>();

        attributes.add("Intelligence");
        attributes.add("Perception");
        attributes.add("Strength");
        attributes.add("Stamina");
        attributes.add("Presence");
        attributes.add("Communication");
        attributes.add("Dexterity");
        attributes.add("Quickness");

        return attributes;
    }
    
    public static int abilityExperienceToScore(int experience){
        Logger logger = Logger.getLogger(characterUtils.class.getName());
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
}
