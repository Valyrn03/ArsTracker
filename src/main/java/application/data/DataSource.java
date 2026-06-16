package application.data;

import application.models.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public class DataSource implements IDataSource{
    private HikariConfig config;
    private HikariDataSource source;

    public DataSource(){
        config = new HikariConfig(DataSource.class.getResource(".properties").toString());

        source = new HikariDataSource(config);
    }

    private Connection getConnection(){
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

    @Override
    public Optional<ArsCharacter> loadCharacterFromId(String characterID){
        Connection connection = this.getConnection();
        ResultSet resultSet = null;
        ResultSetMetaData metadata = null;
        Map<String, String> characterDetails = new HashMap<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE id = ?")){
            statement.setString(0, characterID);

            resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.info("Character with id {} does not exist", characterID);
                resultSet.close();
                return Optional.empty();
            }
            resultSet.first();

            metadata = resultSet.getMetaData();
            for(int i = 0; i < metadata.getColumnCount(); i++){
                characterDetails.put(metadata.getColumnName(i), resultSet.getString(i));
            }
        }catch (SQLException exp){
            log.error("Loading Character with ID {} failed with the following error:\n{}", characterID, exp.getMessage());
            try{
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException ex){
                log.error("\tFailed to close open section");
            }

            try{
                connection.close();
            }catch (SQLException ex){
                log.error("\tFailed to close connection");
            }

            characterDetails = null;
        }

        if(characterDetails != null){
            return Optional.of(ArsCharacter.buildCharacterFromMap(characterDetails, null)); //Need to remember why I passed in a Campaign object
        }else{
            return Optional.empty();
        }
    }

    @Override
    public Optional<Campaign> loadCampaignFromId(String campaignID){
        Connection connection = this.getConnection();
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;
        Map<String, String> campaignDetails = new HashMap<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM campaign WHERE id = ?")){
            statement.setString(0, campaignID);

            resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.info("Campaign with name {} does not exist", campaignID);
                resultSet.close();
                return Optional.empty();
            }
            resultSet.first();

            metaData = resultSet.getMetaData();
            for(int i = 0; i < metaData.getColumnCount(); i++){
                campaignDetails.put(metaData.getColumnName(i), resultSet.getString(i));
            }
        }catch (SQLException exp){
            log.error("Loading campaign with ID {} failed with the following error: \n{}", campaignID, exp.getMessage());
            try{
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException ex){
                log.error("\tFailed to close result set");
            }

            try{
                connection.close();
            }catch (SQLException ex){
                log.error("\tFailed to close connection");
            }

            campaignDetails = null;
        }

        if(campaignDetails != null){
            return Optional.of(Campaign.buildCampaign(campaignDetails));
        }else{
            return Optional.empty();
        }
    }

    @Override
    public boolean updateCharacter(ArsCharacter character) {
        return false;
    }

    @Override
    public boolean updateCampaign(Campaign campaign){
        return false;
    }

    @Override
    public List<Campaign> getCampaigns(){
        List<Campaign> campaigns = new ArrayList<>();
        ResultSet resultSet = null;
        Connection connection = getConnection();
        ResultSetMetaData metaData = null;

        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM campaign LIMIT 10")){
            resultSet = statement.executeQuery();

            metaData = resultSet.getMetaData();

            if(!resultSet.isBeforeFirst()){
                log.error("Result Set is Empty");

                resultSet.close();
                connection.close();
                return campaigns;
            }

            while(resultSet.next()){
                Map<String, String> map = new HashMap<>();
                for(int i = 0; i < metaData.getColumnCount(); i++){
                    map.put(metaData.getColumnName(i), resultSet.getString(i));
                }

                Campaign campaign = Campaign.buildCampaign(map);
                campaigns.add(campaign);
            }
        }catch (SQLException exp){
            log.error("Failed with load campaigns with error {}", exp.getMessage());

            try{
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException ex){
                log.error("\tFailed to close result set");
            }

            try{
                connection.close();
            }catch (SQLException ex){
                log.error("\tFailed to close connection");
            }
        }

        return campaigns;
    }

    /*
    Load from the `ability_tracker` table to get which type of ability and the specific instance
        `ability_category` stores which ability, as read from the list of abilities in the rulebook
        `ability` stores a specific instance, for example if there's multiple options
     */
    @Override
    public List<Ability> loadAbilitiesFromCharacter(String characterID) {
        return List.of();
    }

    /*
    Query `applied_feature` table to get the features the given character has
        Then the `feature` table to get the important information
        And `feature_rule` or `ability_feature_rule` to get how it specifically effects the character
            Numerically
     */
    @Override
    public List<CharacterFeature> loadFeaturesFromCharacter(String characterID) {
        return List.of();
    }

    @Override
    public Optional<Book> loadBookFromId(String bookID) {
        return Optional.empty();
    }

    /*
    Query `applied_covenant_feature` to get the IDs of the features belonging to the covenant
        Then `covenant_feature` to get the important details
     */
    @Override
    public List<CovenantFeature> loadFeaturesFromCovenant(String covenantID) {
        return List.of();
    }

    @Override
    public List<Book> loadBooksFromCovenant(String covenantID) {
        return List.of();
    }
}
