package application.data;

import application.models.Book;
import application.models.Campaign;
import application.models.Covenant;
import application.models.CovenantFeature;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class CovenantDataSource implements ICovenantDataSource{
    private IDataSource source;

    public CovenantDataSource(IDataSource src){
        this.source = src;
    }

    @Override
    public Optional<Book> loadBookFromId(String bookID) {
        return Optional.empty();
    }

    @Override
    public Optional<CovenantFeature> loadCovenantFeatureFromId(String featureID) {
        ResultSet resultSet = null;
        CovenantFeature feature = null;

        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM covenant_feature WHERE id = ?")){
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

        return Optional.ofNullable(feature);
    }

    @Override
    public List<CovenantFeature> loadFeaturesFromCovenant(Covenant covenant) {
        ResultSet resultSet = null;
        List<String> featureIDs = new ArrayList<>();

        //Load the IDs of the features that belong to the given covenant
        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT feature_id FROM applied_covenant_feature WHERE campaign_id = ?")){
            statement.setString(0, String.valueOf(covenant.getId()));

            resultSet = statement.executeQuery();
            while(resultSet.next()){
                featureIDs.add(resultSet.getString("feature_id"));
            }
        }catch (SQLException exp){
            log.error("Failed to load features belonging to covenant with id {}", covenant.getId());
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
    public List<Book> loadBooksFromCovenant(Covenant covenant) {
        return List.of();
    }

    @Override
    public Optional<Covenant> loadCovenantFromId(int covenantID) {
        ResultSet resultSet = null;
        Covenant covenant = null;

        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM covenant WHERE id = ?")){
            statement.setInt(0, covenantID);

            resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.info("Covenant with ID {} does not exist", covenantID);
            }
            resultSet.first();

            Map<String, String> stringMap = new HashMap<>();
            Map<String, Integer> integerMap = new HashMap<>();

            stringMap.put("name", resultSet.getString("name"));
            stringMap.put("tribunal", resultSet.getString("tribunal"));

            integerMap.put("id", resultSet.getInt("id"));
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

        return Optional.ofNullable(covenant);
    }

    @Override
    public boolean updateCovenant(Covenant covenant) {
        return false;
    }

    //Use the campaign's name (b/c primary key)
    @Override
    public boolean addCovenant(Covenant covenant, Campaign campaign) {
        return false;
    }

    @Override
    public boolean addNewCovenantFeature(CovenantFeature feature) {
        return false;
    }

    @Override
    public boolean addFeatureToCovenant(Covenant covenant, CovenantFeature feature) {
        return false;
    }

    @Override
    public Optional<CovenantFeature> getCovenantFeatureById(int id) {
        return Optional.empty();
    }
}
