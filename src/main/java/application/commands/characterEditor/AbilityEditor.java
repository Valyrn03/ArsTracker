package application.commands.characterEditor;

import application.characters.Ability;
import application.characters.Character;
import application.terminal.DataSource;
import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbilityEditor {
    static final Logger logger = LoggerFactory.getLogger(AbilityEditor.class);

    public static boolean isCategorical(String selectedAbility) {
        String query = String.format("SELECT isCategorical FROM ability_category WHERE name = %s", selectedAbility);

        ResultSet resultSet = DataSource.query(query);
        if(resultSet == null){
            try{
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                logger.warn(() -> "Selecting Categorical Query has Failed");
            }
        }

        try{
            return resultSet.getInt(0) == 1;
        }catch (SQLException exp){
            logger.warn(() -> "Categorical Query has failed to determine whether given ability is categorical");
            return false;
        }
    }

    /**
     * Gets the category of ability required, and returns a list of those
     *     The names of each ability, to be precise
     *
     * Will use the character selected to determine which abilities are allowed
     *
     * For now, will allow for only General & Academic abilities
     *
     * @return list of ability names
     */
    public static List<String> getAbilityOptions(Character character) {
        List<String> abilityType = getCharacterCategories(character);

        String query = String.format("SELECT name FROM ability_category WHERE ability_type IN (%s);", listToSql(abilityType));

        List<String> abilities = new ArrayList<>();
        ResultSet abilityResultSet = null;
        try{
            abilityResultSet = DataSource.query(query);

            //There needs to be a better way to do this, but I do not know what it is
            while(!abilityResultSet.isAfterLast()){
                abilities.add(abilityResultSet.getString(0));
                abilityResultSet.next();
            }

            abilityResultSet.close();
        } catch (SQLException e) {
            try{
                try {
                    abilityResultSet.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }finally{
                logger.warn(() -> "Get Ability Options Query has failed");
            }
            abilityResultSet = null;
            throw new RuntimeException(e);
        }

        logger.info(() -> "Ability Options:" + abilities.toString());
        return abilities;
    }

    /**
     * Gets the categories of abilities the character is able to choose from
     *
     * @return a list of categories
     */
    private static List<String> getCharacterCategories(Character character) {
        List<String> list = new ArrayList<>();

        list.add("General");
        list.add("Academic");

        return list;
    }

    /**
     * Simply meant to remove the opening and closing brackets, so that the line this is in is more readable.
     *
     * @param abilityType is the list that requires being pruned
     * @return the toString of the given list, minus the brackets
     */
    public static String listToSql(List<String> abilityType) {
        String listRepr = abilityType.toString();
        return listRepr.substring(1, listRepr.length() - 1);
    }

    /**
     * Will handle the actual database access resulting from calling createAbility()
     *
     * @return whether the query succeeds
     */
    public static boolean addAbilityToDatabase(Character character, Ability ability){
        String query = String.format("INSERT INTO ability_tracker(name, player_id, ability_id, category_id, experience) VALUES (%s, %s, %s, %s, %d);", character.getName(), character.getID(), 0, ability.getCategory(), ability.getExperience());

        try{
            return DataSource.post(query);
        }catch (RuntimeException exp){
            return false;
        }
    }

    /**
     * In the case of the ability already existing, this method will handle the increase in that ability
     *
     * @return the new form of the given ability
     */
    public static Ability editAbility(Ability ability){
        return null;
    }

    /**
     * Verifies if a given character can take a given Ability (ie no duplicates), as well as inserting it if so
     */
    public static boolean verifyAbility(Character character, Ability ability){
        return true;
    }
}
