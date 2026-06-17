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
        }

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

    @Override
    public Optional<CovenantFeature> loadCovenantFeatureFromId(String featureID) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        CovenantFeature feature = null;

        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM covenant_feature WHERE id = ?")){
            statement.setString(0, featureID);

            resultSet = statement.executeQuery();
            resultSet.first();

            Map<String, String> map = new HashMap<>();
            map.put("id", resultSet.getString("id"));
            map.put("name", resultSet.getString("name"));
            map.put("description", resultSet.getString("description"));
            map.put("type", resultSet.getString("isBoon"));
            map.put("isMajor", resultSet.getString("isMajor"));

            feature = new CovenantFeature(map);
        }catch (SQLException exp){
            log.error("Failed to load feature with id {}", featureID);
            feature = null;
        }

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

        return Optional.ofNullable(feature);
    }

    /*
    Query `applied_covenant_feature` to get the IDs of the features belonging to the covenant
        Then `covenant_feature` to get the important details

    Has to be separately added to the given Covenant object
     */
    @Override
    public List<CovenantFeature> loadFeaturesFromCovenant(String covenantID) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        List<String> featureIDs = new ArrayList<>();

        //Load the IDs of the features that belong to the given covenant
        try(PreparedStatement statement = connection.prepareStatement("SELECT feature_id FROM applied_covenant_feature WHERE campaign_id = ?")){
            statement.setString(0, covenantID);

            resultSet = statement.executeQuery();
            while(resultSet.next()){
                featureIDs.add(resultSet.getString("feature_id"));
            }
        }catch (SQLException exp){
            log.error("Failed to load features belonging to covenant with id {}", covenantID);
            featureIDs.clear();
        }

        try{
            if(resultSet != null){
                resultSet.close();
            }
        }catch (SQLException ex){
            log.error("\tFailed to close result set");
            featureIDs.clear();
        }

        try{
            connection.close();
        }catch (SQLException ex){
            log.error("\tFailed to close connection");
            featureIDs.clear();
        }

        //If there was an error or none where found, return early
        if(featureIDs.isEmpty()){
            return new ArrayList<>();
        }

        //Create a new list with the features themselves, load as many as possible from the DB and add them to the list
        List<CovenantFeature> features = new ArrayList<>();

        for(String id : featureIDs){
            loadCovenantFeatureFromId(id).map(features::add);
        }

        return features;
    }

    @Override
    public List<Book> loadBooksFromCovenant(String covenantID) {
        return List.of();
    }

    @Override
    public Optional<Covenant> loadCovenantFromId(String covenantID) {
        Connection connection = getConnection();
        ResultSet resultSet = null;
        Covenant covenant = null;

        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM covenant WHERE id = ?")){
            statement.setString(0, covenantID);

            resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.info("Covenant with ID {} does not exist", covenantID);
            }
            resultSet.first();

            Map<String, String> stringMap = new HashMap<>();
            Map<String, Integer> integerMap = new HashMap<>();

            stringMap.put("id", resultSet.getString("id"));
            stringMap.put("name", resultSet.getString("name"));
            stringMap.put("tribunal", resultSet.getString("tribunal"));

            integerMap.put("establishSeason", resultSet.getInt("establishSeason"));
            integerMap.put("CrVis", resultSet.getInt("CrVis"));
            integerMap.put("InVis", resultSet.getInt("InVis"));
            integerMap.put("MuVis", resultSet.getInt("MuVis"));
            integerMap.put("PeVis", resultSet.getInt("PeVis"));
            integerMap.put("ReVis", resultSet.getInt("ReVis"));
            integerMap.put("AnVis", resultSet.getInt("AnVis"));
            integerMap.put("AuVis", resultSet.getInt("AuVis"));
            integerMap.put("AqVis", resultSet.getInt("AqVis"));
            integerMap.put("CoVis", resultSet.getInt("CoVis"));
            integerMap.put("HeVis", resultSet.getInt("HeVis"));
            integerMap.put("IgVis", resultSet.getInt("IgVis"));
            integerMap.put("ImVis", resultSet.getInt("ImVis"));
            integerMap.put("MeVis", resultSet.getInt("MeVis"));
            integerMap.put("TeVis", resultSet.getInt("TeVis"));
            integerMap.put("ViVis", resultSet.getInt("ViVis"));

            covenant = Covenant.buildCovenantFromMap(stringMap, integerMap);
        }catch (SQLException ex){
            log.error("Failed to load covenant from ID {}", covenantID);
        }

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

        return Optional.ofNullable(covenant);
    }
}
