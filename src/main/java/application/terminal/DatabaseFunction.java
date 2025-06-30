package application.terminal;

import application.characters.Character;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Tables:
 * Campaigns: Characters
 * Character: CampaignID, Characteristics, Birth Season (in months from 0AD), Type, Standard Ability(Serialized), Virtues, Flaws
 * Characteristics: CharacterID, Int, Per, Str, Sta, Pre, Com, Dex, Qik
 * Categorical Abilities: CharacterID(Key), Category, Actual Ability
 * Virtues & Flaws: ???
 */
public class DatabaseFunction {
    private Logger logger;
    String databaseURL;
    public DatabaseFunction(){
        logger = Logger.getLogger(DatabaseFunction.class.getName());
        databaseURL = "jdbc:sqlite:" + DatabaseFunction.class.getResource("arsTrackerDB").getPath();
    }

    public ArrayList<Character> connectCharacterDatabase(){
        Connection connection;
        try{
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException e){
            logger.severe("Failed to Connect to DB");
            return null;
        }

        String createTableStatement = "CREATE TABLE IF NOT EXISTS characters (id BLOB PRIMARY KEY," +
                "name TEXT," +
                "campaignID BLOB," +
                "characteristicsID BLOB," +
                "type TINYINT," +
                "abilityID BLOB," +
                "birthSeason INT," +
                "virtues TEXT," +
                "flaws TEXT)";

        Statement statement;
        try{
            statement = connection.createStatement();
            statement.executeUpdate(createTableStatement);
        }catch (SQLException exp){
            logger.info("Failed to Connect to Character DB");
            return null;
        }

        logger.info("Connected to Character Table");

        String getPlayers = "SELECT * FROM characters";

        ResultSet result;
        ArrayList<Character> characters = new ArrayList<>();
        try{
            result = statement.executeQuery(getPlayers);

            while(result.next()){
                String name = result.getString("name");
                int ageMonths = result.getInt("birthSeason");
                String type = result.getString("type");
                characters.add(new Character(name, ageMonths, type));
                logger.info("Character: " + name);
            }
        } catch (SQLException e) {
            logger.info("Failed to Get Characters");
            return null;
        }

        try{
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return characters;
    }

    public boolean connectAbilityDatabase(){
        return false;
    }

    public boolean add(Character character){
        return false;
    }

    public ArrayList<Character> query(String s) {
        return new ArrayList<>();
    }
}
