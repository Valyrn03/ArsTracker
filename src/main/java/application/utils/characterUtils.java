package application.utils;

import java.util.logging.Logger;

public class characterUtils {
    public enum Attribute {
        INTELLIGENCE, PERCEPTION, STRENGTH, STAMINA, PRESENCE, COMMUNICATION, DEXTERITY, QUICKNESS;
    }

    public enum ExtraneousAttribute {
        AGE, SIZE, CONFIDENCE_SCORE, CONFIDENCE_POINTS, DECREPITUDE, WARPING
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
