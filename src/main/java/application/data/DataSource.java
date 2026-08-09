package application.data;

import application.models.Ability;
import application.models.ArsCharacter;
import application.models.Campaign;
import application.models.Covenant;
import application.models.enums.AbilityCategory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.UpdateSummaryEnum;
import liquibase.UpdateSummaryOutputEnum;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.ui.LoggerUIService;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import static application.models.Ability.*;

@Slf4j
public class DataSource implements IDataSource{
    private HikariConfig config;
    private HikariDataSource source;

    public DataSource(Properties props){
        config = new HikariConfig(props);
        source = new HikariDataSource(config);

        updateFromLiquibase();
        populateTable();
    }

    public DataSource(){
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
            Liquibase liquibase = new Liquibase(
                    "ars-tracker-changelog.sql",
                    new DirectoryResourceAccessor(Paths.get("")),
                    database
            );
            Scope.enter(Map.of(Scope.Attr.ui.name(), new NullUIService()));
            liquibase.update("--logLevel=OFF");
        }catch (SQLException exp){
            log.error("Failed to update from liquibase with error {}", exp.getMessage());
        }catch (DatabaseException exp){
            log.error("Failed to update database {}", exp.getMessage());
        }catch (LiquibaseException exp){
            log.error("Liquibase error {}", exp.getMessage());
        }catch (FileNotFoundException exp){
            log.error("Failed to find repo root, {}", exp.getMessage());
        }catch (Exception exp){
            log.error("Error, {}", exp.getMessage());
        }
    }

    private static class NullUIService extends LoggerUIService {
        @Override
        public void sendMessage(String message){

        }

        @Override
        public void sendErrorMessage(String message, Throwable exception){
            log.error(message);
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
        if(ability == null || id == 0){
            return false;
        }

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ability (owner_id, ability, category, speciality, experience) VALUES (?, ?, ?, ?, ?)")){
            statement.setInt(1, id);
            statement.setString(2, ability.getAbility());
            statement.setString(3, ability.getCategory().toString());
            statement.setString(4, ability.getSpeciality());
            statement.setInt(5, ability.getExperience());

            statement.execute();
        }catch (SQLException exp){
            log.error("Failed to add ability {} to {} with error {}", ability, id, exp.getMessage());
            return false;
        }
        return true;
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
            PreparedStatement statement = conn.prepareStatement("INSERT OR IGNORE INTO ability_category (ability, category) VALUES (?, ?)")){
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

    @Override
    public boolean deleteCampaign(Campaign campaign) {
        boolean deletedCovenants = campaign.getCovenants().stream().map(this::deleteCovenant).toList().contains(false);
        if(!deletedCovenants){
            return false;
        }

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("")){

        }catch (SQLException exp){
            log.error("Failed to delete campaign with error {}", exp.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteCovenant(Covenant covenant) {
        boolean deletedCharacters = covenant.getPlayerCharacters().stream().map(this::deleteCharacter).toList().contains(false);
        if(!deletedCharacters){
            return false;
        }

        try(Connection connection = getConnection();
            PreparedStatement featureStatement = connection.prepareStatement("DELETE FROM applied_covenant_feature WHERE id = ?");
            PreparedStatement visStatement = connection.prepareStatement("DELETE FROM vis WHERE covenant_id = ?");
            PreparedStatement covenantStatement = connection.prepareStatement("DELETE FROM covenant WHERE id = ?")){
            featureStatement.setInt(1, covenant.getId());
            visStatement.setInt(1, covenant.getId());
            covenantStatement.setInt(1, covenant.getId());

            featureStatement.execute();
            visStatement.execute();
            covenantStatement.execute();
        }catch (SQLException exp){
            log.error("Failed to delete covenant with error {}", exp.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteCharacter(ArsCharacter character) {
        try(Connection connection = getConnection();
            PreparedStatement featureStatement = connection.prepareStatement("DELETE FROM applied_feature WHERE player_id = ?");
            PreparedStatement abilityStatement = connection.prepareStatement("DELETE FROM ability WHERE owner_id = ?");
            PreparedStatement visStatement = connection.prepareStatement("DELETE FROM arts WHERE character_id = ?");
            PreparedStatement characterStatement = connection.prepareStatement("DELETE FROM character WHERE id = ?")){
            featureStatement.setInt(1, character.getId());
            abilityStatement.setInt(1, character.getId());
            visStatement.setInt(1, character.getId());
            characterStatement.setInt(1, character.getId());

            featureStatement.execute();
            abilityStatement.execute();
            visStatement.execute();
            characterStatement.execute();
        }catch (SQLException exp){
            log.error("Failed to delete character {} with error {}", character.toString(), exp.getMessage());
            return false;
        }

        return true;
    }
}
