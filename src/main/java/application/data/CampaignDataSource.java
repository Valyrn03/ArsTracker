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
    public Optional<Campaign> loadCampaignFromId(String campaignID) {
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;
        Map<String, String> campaignDetails = new HashMap<>();
        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM campaign WHERE id = ?")){
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

        return campaigns;
    }

    @Override
    public List<Covenant> loadCovenantsFromCampaign(Campaign campaign) {
        return List.of();
    }
}
