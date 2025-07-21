package application;

import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Scripts {
    public static void main(String[] args) {
        if(resetGeneral()){
            System.out.println("General Abilities Set");
        }
        if(resetAcademic()){
            System.out.println("Academic Abilities Set");
        }
        if(resetArcane()){
            System.out.println("Arcane Abilities Set");
        }
        if(resetSupernatural()){
            System.out.println("Supernatural Abilities Set");
        }
        if(resetMartial()){
            System.out.println("Martial Abilities Set");
        }
    }

    private static boolean resetMartial(){
        String databaseURL;
        try(InputStream stream = Launcher.class.getResourceAsStream(".properties")){
            Properties properties = new Properties();
            properties.load(stream);
            if("test".equals(properties.getProperty("type"))){
                databaseURL = "jdbc:sqlite:" + properties.getProperty("testDBPath");
            }else{
                databaseURL = "jdbc:sqlite:" + properties.getProperty("prodDBPath");
            }
        }catch (IOException exp){
            System.out.println("Failed to Find Database Path");
            return false;
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException exp){
            System.out.println("Failed to Connect to Database");
            return false;
        }

        List<String> martialAbilities = new ArrayList<>();
        martialAbilities.add("Bows");
        martialAbilities.add("Great Weapon");
        martialAbilities.add("Single Weapon");
        martialAbilities.add("Thrown Weapon");

        String standardInsertionCall = "INSERT OR IGNORE INTO ability_category(name, overarchingType) VALUES (%s, %s);";

        try{
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(String.format("Failed to Update Table, Exception: %s", e.getMessage()));
            return false;
        }

        try{
            connection.close();
            return true;
        }catch (SQLException exp){
            System.out.println("Failed to Close Connection");
            return true;
        }
    }

    private static boolean resetGeneral(){
        String databaseURL;
        try(InputStream stream = Launcher.class.getResourceAsStream(".properties")){
            Properties properties = new Properties();
            properties.load(stream);
            if("test".equals(properties.getProperty("type"))){
                databaseURL = "jdbc:sqlite:" + properties.getProperty("testDBPath");
            }else{
                databaseURL = "jdbc:sqlite:" + properties.getProperty("prodDBPath");
            }
        }catch (IOException exp){
            System.out.println("Failed to Find Database Path");
            return false;
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException exp){
            System.out.println("Failed to Connect to Database");
            return false;
        }

        String standardInsertionCall = "INSERT OR IGNORE  INTO ability_category (name, overarchingType) VALUES ('%s', '%s');";

        try{
            Statement statement = connection.createStatement();

            for(String ability : generalAbilities()){
                statement.executeUpdate(String.format(standardInsertionCall, ability, "general"));
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println(String.format("Failed to Update Table, Exception: %s", e.getMessage()));
            return false;
        }

        try{
            connection.close();
            return true;
        }catch (SQLException exp){
            System.out.println("Failed to Close Connection");
            return true;
        }
    }

    private static boolean resetAcademic(){
        String databaseURL;
        try(InputStream stream = Launcher.class.getResourceAsStream(".properties")){
            Properties properties = new Properties();
            properties.load(stream);
            if("test".equals(properties.getProperty("type"))){
                databaseURL = "jdbc:sqlite:" + properties.getProperty("testDBPath");
            }else{
                databaseURL = "jdbc:sqlite:" + properties.getProperty("prodDBPath");
            }
        }catch (IOException exp){
            System.out.println("Failed to Find Database Path");
            return false;
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException exp){
            System.out.println("Failed to Connect to Database");
            return false;
        }

        String standardInsertionCall = "INSERT OR IGNORE INTO ability_category(name, overarchingType) VALUES ('%s', '%s');";

        try{
            Statement statement = connection.createStatement();

            for(String ability : academicAbilities()){
                statement.executeUpdate(String.format(standardInsertionCall, ability, "academic"));
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println(String.format("Failed to Update Table, Exception: %s", e.getMessage()));
            return false;
        }

        try{
            connection.close();
            return true;
        }catch (SQLException exp){
            System.out.println("Failed to Close Connection");
            return true;
        }
    }

    private static boolean resetArcane(){
        String databaseURL;
        try(InputStream stream = Launcher.class.getResourceAsStream(".properties")){
            Properties properties = new Properties();
            properties.load(stream);
            if("test".equals(properties.getProperty("type"))){
                databaseURL = "jdbc:sqlite:" + properties.getProperty("testDBPath");
            }else{
                databaseURL = "jdbc:sqlite:" + properties.getProperty("prodDBPath");
            }
        }catch (IOException exp){
            System.out.println("Failed to Find Database Path");
            return false;
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException exp){
            System.out.println("Failed to Connect to Database");
            return false;
        }

        String standardInsertionCall = "INSERT OR IGNORE INTO ability_category(name, overarchingType) VALUES ('%s', '%s');";

        try{
            Statement statement = connection.createStatement();

            for(String ability : arcaneAbilities()){
                statement.executeUpdate(String.format(standardInsertionCall, ability, "arcane"));
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println(String.format("Failed to Update Table, Exception: %s", e.getMessage()));
            return false;
        }

        try{
            connection.close();
            return true;
        }catch (SQLException exp){
            System.out.println("Failed to Close Connection");
            return true;
        }
    }

    private static boolean resetSupernatural(){
        String databaseURL;
        try(InputStream stream = Launcher.class.getResourceAsStream(".properties")){
            Properties properties = new Properties();
            properties.load(stream);
            if("test".equals(properties.getProperty("type"))){
                databaseURL = "jdbc:sqlite:" + properties.getProperty("testDBPath");
            }else{
                databaseURL = "jdbc:sqlite:" + properties.getProperty("prodDBPath");
            }
        }catch (IOException exp){
            System.out.println("Failed to Find Database Path");
            return false;
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(databaseURL);
        }catch (SQLException exp){
            System.out.println("Failed to Connect to Database");
            return false;
        }

        String standardInsertionCall = "INSERT OR IGNORE INTO ability_category(name, overarchingType) VALUES ('%s', '%s');";

        try{
            Statement statement = connection.createStatement();

            for(String ability : supernaturalAbilities()){
                statement.executeUpdate(String.format(standardInsertionCall, ability, "supernatural"));
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println(String.format("Failed to Update Table, Exception: %s", e.getMessage()));
            return false;
        }

        try{
            connection.close();
            return true;
        }catch (SQLException exp){
            System.out.println("Failed to Close Connection");
            return true;
        }
    }

    private static List<String> generalAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Athletics");
        list.add("Animal Handling");
        list.add("(Area) Lore");
        list.add("Awareness");
        list.add("Bargain");
        list.add("Brawl");
        list.add("Carouse");
        list.add("Charm");
        list.add("Chirurgy");
        list.add("Concentration");
        list.add("Craft (Type)");
        list.add("Etiquette");
        list.add("Folk Ken");
        list.add("Guile");
        list.add("Hunt");
        list.add("Intrigue");
        list.add("Judaic Lore");
        list.add("Leadership");
        list.add("Legerdemain");
        list.add("(Living Language)");
        list.add("Music");
        list.add("(Mystery Cult) Lore");
        list.add("(Organization) Lore");
        list.add("Profession (Type)");
        list.add("Ride");
        list.add("Stealth");
        list.add("Survival");
        list.add("Swim");
        list.add("Teaching");

        return list;
    }

    private static List<String> academicAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Medicine");
        list.add("Art of Memory");
        list.add("Artes Liberales");
        list.add("Civil and Canon Law");
        list.add("Common Law");
        list.add("(Dead Language)");
        list.add("Islamic Law");
        list.add("Philosophiae");
        list.add("Rabbinic Law");
        list.add("Theology: Christian");
        list.add("Theology: Islam");
        list.add("Theology: Judaism");

        return list;
    }

    private static List<String> arcaneAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Penetration");
        list.add("Code of Hermes");
        list.add("Dominion Lore");
        list.add("Enigmatic Wisdom");
        list.add("Faerie Lore");
        list.add("Faerie Magic");
        list.add("Finesse");
        list.add("Heartbeast");
        list.add("Infernal Lore");
        list.add("Magic Lore");
        list.add("Magic Theory");
        list.add("Parma Magica");

        return list;
    }

    private static List<String> supernaturalAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Dowsing");
        list.add("Animal Ken");
        list.add("Corpse Magic");
        list.add("Crafters Healing");
        list.add("Curse-Throwing");
        list.add("Embitterment");
        list.add("Enchanting (Ability)");
        list.add("Entrancement");
        list.add("Font of Knowledge");
        list.add("Hex");
        list.add("Induction");
        list.add("Magic Sensitivity");
        list.add("Persona");
        list.add("Premonitions");
        list.add("Second Sight");
        list.add("Sense Holiness and Unholiness");
        list.add("Sense Passions");
        list.add("Shapeshifter");
        list.add("Summon Animals");
        list.add("Whistle Up The Wind");
        list.add("Wilderness Sense");

        return list;
    }
}
