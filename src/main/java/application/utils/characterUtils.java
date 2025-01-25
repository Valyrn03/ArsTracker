package application.utils;

import java.util.ArrayList;
import java.util.logging.Logger;

public class characterUtils {
    public enum AttributeList {
        INTELLIGENCE, PERCEPTION, STRENGTH, STAMINA, PRESENCE, COMMUNICATION, DEXTERITY, QUICKNESS;
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
