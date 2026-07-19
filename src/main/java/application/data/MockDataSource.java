package application.data;

import application.models.*;
import application.models.enums.AbilityCategory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import static application.models.Ability.*;

@Slf4j
public class MockDataSource implements IDataSource{
    private HikariConfig config;
    private HikariDataSource source;

    public MockDataSource(){
//        config = new HikariConfig(String.valueOf(DataSource.class.getResource("properties")));
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.sqlite.SQLiteDataSource");
        props.setProperty("dataSource.databaseName", "prodDB");
        props.setProperty("jdbcUrl", "jdbc:sqlite:");
        props.setProperty("maximumPoolSize", "1");

        config = new HikariConfig(props);
        source = new HikariDataSource(config);

        updateFromLiquibase();
        populateDefaultTable();
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
    public void close() {

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

    @Override
    public List<Ability> loadAbilitiesById(int id) {
        List<Ability> abilities = new ArrayList<>();

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ability WHERE owner_id = ?")){
            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    String abilitySubtype = resultSet.getString("ability");
                    AbilityCategory category = AbilityCategory.valueOf(resultSet.getString("category"));
                    String speciality = resultSet.getString("speciality");
                    int exp = resultSet.getInt("experience");

                    Ability ability = new Ability(category, abilitySubtype, speciality, exp);
                    abilities.add(ability);
                }
            }
        }catch (SQLException exp){
            log.error("Failed to load abilities for id {} with error {}", id, exp.getMessage());
            return Collections.emptyList();
        }

        if(abilities.isEmpty()){
            log.info("No abilities were found in relation to id {}", id);
        }

        return abilities;
    }

    @Override
    public boolean addAbility(int id, Ability ability) {
        return false;
    }

    private void populateDefaultTable(){
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
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO ability_category(ability, category) VALUES (?, ?)")){
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

    /*

     */
    private void populateInitialObjects(){
        try(Connection connection = getConnection()){

        }catch (SQLException e){
            log.error("Failed to open connection with the following error: {}", e.getMessage());
        }
    }
}
