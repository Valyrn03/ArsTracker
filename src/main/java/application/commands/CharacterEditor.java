package application.commands;

import application.Launcher;
import application.characters.Ability;
import application.characters.Attribute;
import application.characters.Character;
import application.terminal.CharacterController;
import org.beryx.textio.TextIO;
import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
Steps in Character Creation:

1. Virtues & Flaws
2. Characteristics X
3. Early Childhood
4. Later Life / Apprenticeship
    4.5 Spells
5. Personality

Arts & Characteristics Formula: n(n+1)/2
Abilities Formula: 5n(n+1)/2 OR 5*arts
    Cost to raise to tier: 5*n
 */
public class CharacterEditor extends CharacterController {
    static final Logger logger = LoggerFactory.getLogger(CharacterEditor.class);
    private Character character;

    public CharacterEditor(TextIO source, ArrayList<Character> arr, Character character) {
        super(source, arr);
    }

    @Override
    public boolean execute() {
        return false;
    }

    public void changeCharacter(Character newCharacter){
        character = newCharacter;
    }

    /**
     * CHARACTERISTICS
     *
     * Handles user input & repeating if there is an issue
     *
     * @param prompt is the message that should be passed to the user, in order to use recursive calls with different messages
     *
     * @return a list of valid characteristics
     */
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
            int pointsValue = calculateCost(characteristicValue);
            if(characteristicValue > 0){
                pointsValue = pointsValue * -1;
            }
            costs.add(pointsValue);
            points += pointsValue;
        }

        int finalPoints = points;
        logger.info(() -> "Array: " + characteristics.toString() + "\nCosts: " + costs.toString() + "\nPoints:" + finalPoints);
        if(points >= 0){
            return null;
        }
        return costs;
    }

    /**
     * Method to handle the creation of a new ability.
     *
     * Will need to get the category, then if it's a categorical ability the exact kind. Then the XP value
     *
     * Requirements:
     *     1. Get Overarching Category
     *     2. List all abilities, and allow a selection from those
     *     3. Get subtype, if applicable
     *     4. Get XP value
     *     5. Add to applicable character
     *     6. Call addAbilityToDatabase() in order to add the newly created Ability
     */
    public Ability createAbility(){
        ArrayList<String> abilityOptions = getAbilityOptions();

        assert abilityOptions != null;
        String selectedAbility = abilityOptions.get(super.getOptions(abilityOptions));
        String subtype = null;

        //Need to figure out a good way to check if an ability needs a subtype or not
        if(isCategorical(selectedAbility)){
            subtype = super.getString("\tName of Ability");
        }

        int xpValue = super.getInt("Initial Experience");

        String speciality = super.getString("Speciality");

        Ability ability = Ability.createAbility(selectedAbility, subtype, speciality, xpValue);

        if(ability == null){
            super.print("Error in Creating Ability");
            return null;
        }

        return ability;
    }

    private boolean isCategorical(String selectedAbility) {
        String databaseURL = null;

        try(InputStream stream = Launcher.class.getResourceAsStream(".properties")){
            Properties properties = new Properties();
            properties.load(stream);
            if("test".equals(properties.getProperty("type"))){
                databaseURL = "jdbc:sqlite:" + properties.getProperty("testDBPath");
            }else{
                databaseURL = "jdbc:sqlite:" + properties.getProperty("prodDBPath");
            }
        }catch (IOException exp){
            logger.info(() -> "Failed to Find Properties File");
        }

        Connection connection;
        try{
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException exp){
            logger.info(() -> "Database Failed to Open");
            throw new RuntimeException(exp);
        }

        String query = String.format("SELECT isCategorical FROM ability_category WHERE name = %s", selectedAbility);

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.getInt(0) == 1){
                resultSet.close();
                return true;
            }else{
                resultSet.close();
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    private ArrayList<String> getAbilityOptions() {
        ArrayList<String> abilityType = getCharacterCategories();

        String databaseURL = null;

        try(InputStream stream = Launcher.class.getResourceAsStream(".properties")){
            Properties properties = new Properties();
            properties.load(stream);
            if("test".equals(properties.getProperty("type"))){
                databaseURL = "jdbc:sqlite:" + properties.getProperty("testDBPath");
            }else{
                databaseURL = "jdbc:sqlite:" + properties.getProperty("prodDBPath");
            }
        }catch (IOException exp){
            logger.info(() -> "Failed to Find Properties File");
        }

        Connection connection;
        try{
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException exp){
            logger.info(() -> "Database Failed to Open");
            return null;
        }

        String query = String.format("SELECT name FROM ability_category WHERE ability_type IN (%s);", listToSql(abilityType));

        Statement statement;
        ArrayList<String> abilities = new ArrayList<>();
        try{
            statement = connection.createStatement();
            ResultSet abilityResultSet = statement.executeQuery(query);

            //There needs to be a better way to do this, but I do not know what it is
            while(!abilityResultSet.isAfterLast()){
                abilities.add(abilityResultSet.getString(0));
                abilityResultSet.next();
            }

            abilityResultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logger.info(() -> "Ability Options:" + abilities.toString());
        return abilities;
    }

    /**
     * Gets the categories of abilities the character is able to choose from
     * @return
     */
    private ArrayList<String> getCharacterCategories() {
        ArrayList<String> list = new ArrayList<>();

        list.add("General");
        list.add("Academic");

        return list;
    }

    /**
     * Simply meant to remove the opening and closing brackets, so that the line this is in is more readable.
     * @param abilityType
     * @return
     */
    public String listToSql(ArrayList<String> abilityType) {
        String listRepr = abilityType.toString();
        return listRepr.substring(1, listRepr.length() - 1);
    }

    /**
     * Will handle the actual database access resulting from calling createAbility()
     *
     * @return
     */
    public boolean addAbilityToDatabase(){
        return false;
    }

    /**
     * In the case of the ability already existing, this method will handle the increase in that ability
     * @return
     */
    public Ability editAbility(){
        return null;
    }
}
