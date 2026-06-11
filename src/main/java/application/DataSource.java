package application;

import application.models.Campaign;
import application.models.Character;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public class DataSource {
    private HikariConfig config;
    private HikariDataSource source;

    public DataSource(boolean isMock){
        config = new HikariConfig(DataSource.class.getResource(".properties").toString());
        if(isMock){
            config.setJdbcUrl("jdbc:sqlite:testDB.db");
            config.setUsername("");
            config.setPassword("");
        }

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

    public void close(){
        source.close();
    }

    public Optional<Character> loadCharacterFromId(String characterID){
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
            return Optional.of(Character.buildCharacterFromMap(characterDetails, null)); //Need to remember why I passed in a Campaign object
        }else{
            return Optional.empty();
        }
    }

    /*
    Load campaign table in order to access details about it, getting the characters will be a separate function.
     */
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

    public void updateCharacter(Character character){

    }

    public void updateCampaign(Campaign campaign){

    }

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
    }
}
