package application.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

@Slf4j
public class DataSource implements IDataSource{
    private HikariConfig config;
    private HikariDataSource source;

    public DataSource(){
//        config = new HikariConfig(String.valueOf(DataSource.class.getResource("properties")));
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.sqlite.SQLiteDataSource");
        props.setProperty("dataSource.databaseName", "prodDB");
        props.setProperty("jdbcUrl", "jdbc:sqlite:arsTrackerDB.db");
        props.setProperty("maximumPoolSize", "1");

        config = new HikariConfig(props);
        source = new HikariDataSource(config);

        updateFromLiquibase();
        populateTable();
    }

    @Override
    public Connection getConnection(){
        try{
            return source.getConnection();
        }catch (SQLException exp){
            log.warn("Failed to pull connection from pool: {}", exp.getMessage());
            System.exit(1);
            return null;
        }
    }

    @Override
    public void close(){
        source.close();
    }

    private void updateFromLiquibase(){
        log.debug("Began updating");
        try(Connection connection = getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))){
            log.info("Got database");
            Liquibase liquibase = new Liquibase(
                    "ars-tracker-changelog.sql",
                    new DirectoryResourceAccessor(Paths.get("")),
                    database
            );
            log.info("Constructed liquibase instance");
            liquibase.update("");
            log.info("Updated");
        }catch (SQLException exp){
            log.error("Failed to update from liquibase with error {}", exp.getMessage());
        }catch (DatabaseException exp){
            log.error("Failed to update database {}", exp.getMessage());
        }catch (LiquibaseException exp){
            log.error("Liquibase error {}", exp.getMessage());
        }catch (FileNotFoundException exp){
            log.error("Failed to find repo root, {}", exp.getMessage());
        }
    }

    private void populateTable(){
        resetArts();
        resetAbilities();
    }

    private void resetArts() {
        List<String> techniques = new ArrayList<>();
        techniques.add("Creo");
        techniques.add("Intellego");
        techniques.add("Muto");
        techniques.add("Perdo");
        techniques.add("Rego");

        List<String> forms = new ArrayList<>();
        forms.add("Animal");
        forms.add("Aquam");
        forms.add("Aurum");
        forms.add("Corpus");
        forms.add("Herbam");
        forms.add("Ignem");
        forms.add("Mentem");
        forms.add("Terram");
        forms.add("Vim");

        try(Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement("INSERT OR IGNORE INTO ability_category (name, overarchingType) VALUES (?, ?)")){
            for(String ability : techniques){
                statement.setString(1, ability);
                statement.setString(2, "technique");
                statement.execute();
            }
            for(String ability : forms){
                statement.setString(1, ability);
                statement.setString(2, "form");
                statement.execute();
            }
        } catch (SQLException e) {
            log.error("Failed to Populate Arts Table, Exception: {}", e.getMessage());
        }
    }

    private void resetAbilities(){
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO ability_category(name, overarchingType) VALUES (?, ?)")){
            for(String ability : generalAbilities()){
                statement.setString(1, ability);
                statement.setString(2, "general");
                statement.execute();
            }

            for(String ability : academicAbilities()){
                statement.setString(1, ability);
                statement.setString(2, "academic");
                statement.execute();
            }

            for(String ability : arcaneAbilities()){
                statement.setString(1, ability);
                statement.setString(2, "arcane");
                statement.execute();
            }

            for(String ability : supernaturalAbilities()){
                statement.setString(1, ability);
                statement.setString(2, "supernatural");
                statement.execute();
            }
        }catch (SQLException exp){
            log.error("Failed to populate Abilities table with the following error: {}", exp.getMessage());
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
