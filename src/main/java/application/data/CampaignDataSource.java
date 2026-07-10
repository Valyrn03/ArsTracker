package application.data;

import application.models.Campaign;
import application.models.Covenant;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public class CampaignDataSource implements ICampaignDataSource{
    private IDataSource source;

    public CampaignDataSource(IDataSource src){
        this.source = src;
    }

    @Override
    public Optional<Campaign> loadCampaignFromName(String name) {
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;
        Map<String, String> campaignDetails = new HashMap<>();
        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM campaign WHERE name = ?")){
            statement.setString(1, name);

            resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.info("Campaign {} does not exist", name);
                resultSet.close();
                return Optional.empty();
            }

            metaData = resultSet.getMetaData();
            for(int i = 1; i < metaData.getColumnCount() + 1; i++){
                campaignDetails.put(metaData.getColumnName(i), resultSet.getString(i));
            }
        }catch (SQLException exp){
            log.error("Loading campaign {} failed with the following error: \n{}", name, exp.getMessage());
            try{
                if(resultSet != null){
                    resultSet.close();
                }
            }catch (SQLException ex){
                log.error("\tFailed to close result set");
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
    public boolean updateCampaign(Campaign campaign) {
        return false;
    }

    @Override
    public List<Campaign> getCampaigns() {
        List<Campaign> campaigns = new ArrayList<>();
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;

        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM campaign LIMIT 10")){
            resultSet = statement.executeQuery();

            metaData = resultSet.getMetaData();

            if(!resultSet.isBeforeFirst()){
                log.error("Result Set is Empty");
            }

            while(resultSet.next()){
                Map<String, String> map = new HashMap<>();
                for(int i = 1; i < metaData.getColumnCount() + 1; i++){
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

        return campaigns;
    }

    @Override
    public List<Covenant> loadCovenantsFromCampaign(Campaign campaign) {
        return List.of();
    }

    @Override
    public boolean addCampaign(String name, int seasons) {
        if(seasons < 0 || name == null){
            return false;
        }

        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("INSERT INTO campaign VALUES (?, ?)")){
            statement.setString(1, name);
            statement.setInt(2, seasons);

            statement.execute();
            return true;
        }catch (SQLException exception){
            log.error("Failed with adding campaign {}, with error {}", name, exception.getMessage());
            return false;
        }
    }
}
