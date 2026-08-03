package application.data;

import application.models.*;
import application.models.enums.Art;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
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
    public Optional<CovenantFeature> loadCovenantFeatureFromId(int featureID) {
        if(featureID == 0){
            return Optional.empty();
        }
        ResultSet resultSet = null;
        CovenantFeature feature = null;

        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM covenant_feature WHERE id = ?")){
            statement.setInt(1, featureID);

            resultSet = statement.executeQuery();

            Map<String, String> map = new HashMap<>();
            map.put("id", resultSet.getString("id"));
            map.put("name", resultSet.getString("name"));
            map.put("description", resultSet.getString("description"));
            map.put("isBoon", resultSet.getString("isBoon"));
            map.put("isMajor", resultSet.getString("isMajor"));

            feature = new CovenantFeature(map);
        }catch (SQLException exp){
            log.error("Failed to load feature with id {} with error {}", featureID, exp.getMessage());
            return Optional.empty();
        }

        try{
            if(resultSet != null){
                resultSet.close();
            }
        }catch (SQLException ex){
            log.error("\tFailed to close result set");
            return Optional.empty();
        }

        return Optional.ofNullable(feature);
    }

    @Override
    public List<CovenantFeature> loadFeaturesFromCovenant(Covenant covenant) {
        if(covenant == null){
            return Collections.emptyList();
        }
        ResultSet resultSet = null;
        List<Integer> featureIDs = new ArrayList<>();

        //Load the IDs of the features that belong to the given covenant
        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT feature_id FROM applied_covenant_feature WHERE covenant_id = ?")){
            statement.setString(1, String.valueOf(covenant.getId()));

            resultSet = statement.executeQuery();
            while(resultSet.next()){
                featureIDs.add(resultSet.getInt("feature_id"));
            }
        }catch (SQLException exp){
            log.error("Failed to load features belonging to covenant with id {}, with error {}", covenant.getId(), exp.getMessage());
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

        for(int id : featureIDs){
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
        if(covenantID == 0){
            return Optional.empty();
        }
        Covenant covenant = null;

        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM covenant WHERE id = ?")){
            statement.setInt(1, covenantID);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                log.info("Covenant with ID {} does not exist", covenantID);
            }

//            resultSet.next();
            Map<String, String> stringMap = new HashMap<>();
            Map<String, Integer> integerMap = new HashMap<>();

            stringMap.put("name", resultSet.getString("name"));
            stringMap.put("tribunal", resultSet.getString("tribunal"));

            integerMap.put("id", resultSet.getInt("id"));
            integerMap.put("establishSeason", resultSet.getInt("establishSeason"));

            covenant = Covenant.buildCovenantFromMap(stringMap, integerMap);
        }catch (SQLException ex){
            log.error("Failed to load covenant from ID {} with error {}", covenantID, ex.getMessage());
            return Optional.empty();
        }

        Map<Art, Integer> visStores = loadCovenantVisStores(covenantID);
        if(!visStores.isEmpty()){
            covenant.setVisStores(visStores);
        }

        return Optional.of(covenant);
    }

    public Map<Art, Integer> loadCovenantVisStores(int covenantId){
        Map<Art, Integer> map = new HashMap<>();
        try(Connection connection = source.getConnection()){
            for(Art art : Art.values()){
                try(PreparedStatement statement = connection.prepareStatement("SELECT value FROM vis WHERE covenant_id = ? AND art = ?")){
                    statement.setInt(1, covenantId);
                    statement.setString(2, art.toString());

                    ResultSet resultSet = statement.executeQuery();
                    map.put(art, resultSet.getInt("value"));
                }
            }
        }catch (SQLException exp){
            log.error("Failed to load vis stores for covenant {} with error {}", covenantId, exp.getMessage());
            return Collections.emptyMap();
        }

        return map;
    }

    @Override
    public boolean updateCovenantVisStores(Covenant covenant, Art art) {
        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE vis SET value = ? WHERE covenant_id = ? AND art = ?")){
            statement.setInt(1, covenant.getVis(art));
            statement.setInt(2, covenant.getId());
            statement.setString(3, art.toString());

            statement.executeUpdate();
        }catch (SQLException exception){
            log.error("Updating covenant {} failed with the error {}", covenant.getName(), exception.getMessage());
            return false;
        }

        return true;
    }

    //Use the campaign's name (b/c primary key)
    @Override
    public boolean addCovenant(Covenant covenant, Campaign campaign) {
        //Check to make sure the given campaign is in the DB, and if not refuse to continue.
        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM campaign WHERE name = ?")){
            statement.setString(1, campaign.getName());
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.error("Campaign {} has not been added previously", campaign.getName());
                return false;
            }
        }catch (SQLException exp){
            log.error("Failed to check campaign status for covenant {} in campaign {}", covenant.getName(), campaign.getName());
        }

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO covenant (name, tribunal, campaign_name, establishSeason) VALUES (?, ?, ?, ?)");
            PreparedStatement idStatement = connection.prepareStatement("SELECT last_insert_rowid()")){
            statement.setString(1, covenant.getName());
            statement.setString(2, covenant.getTribunal().toString());
            statement.setString(3, campaign.getName());
            statement.setInt(4, covenant.getEstablishmentSeason());

            statement.execute();

            ResultSet resultSet = idStatement.executeQuery();
            covenant.setId(resultSet.getInt(1));
        }catch (SQLException exp){
            log.error("Failed to add covenant {} with error {}", covenant.getName(), exp.getMessage());
            return false;
        }

        try(Connection connection = source.getConnection()){
            for(Art art : Art.values()){
                try(PreparedStatement statement = connection.prepareStatement("INSERT INTO vis VALUES (?, ?, ?)")){
                    statement.setInt(1, covenant.getId());
                    statement.setString(2, art.toString());
                    statement.setInt(3, covenant.getVis(art));

                    statement.execute();
                }
            }
        }catch (SQLException exp){
            log.error("Failed to add vis to covenant {} with error {}", covenant.getId(), exp.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean saveCovenantFeature(CovenantFeature feature) {
        if(feature == null){
            return false;
        }

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO covenant_feature (name, description, isBoon, isMajor) VALUES (?, ?, ?, ?)");
            PreparedStatement idStatement = connection.prepareStatement("SELECT last_insert_rowid()")){
            statement.setString(1, feature.getName());
            statement.setString(2, feature.getDescription());

            if(CovenantFeature.FeatureType.BOON.equals(feature.getType())){
                statement.setInt(3, 0);
            }else{
                statement.setInt(3, 1);
            }

            if(feature.isMajor()){
                statement.setInt(4, 0);
            }else{
                statement.setInt(4, 1);
            }

            statement.execute();
            ResultSet resultSet = idStatement.executeQuery();
            feature.setId(resultSet.getInt(1));
        }catch (SQLException exp){
            log.error("Failed to save covenant feature {}, with error {}", feature.getName(), exp.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean addFeatureToCovenant(Covenant covenant, CovenantFeature feature) {
        if(covenant == null || feature == null){
            log.error("Passed in a null value (covenant == null -> {})", covenant == null);
            return false;
        }

        if(feature.getId() == 0){
            log.error("Feature {} has not been loaded in the database", feature.getName());
            return false;
        }

        if(covenant.getId() == 0){
            log.error("Covenant {} has not been loaded in the database", covenant.getName());
            return false;
        }

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO applied_covenant_feature VALUES (?, ?)")){
            statement.setInt(1, covenant.getId());
            statement.setInt(2, feature.getId());

            statement.execute();
        }catch (SQLException exp){
            log.error("Failed to add feature {} to covenant {} with error {}", feature.getName(), covenant.getName(), exp.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean updateCovenantLabTexts(Covenant covenant) {
        return false;
    }

    @Override
    public List<Integer> loadCovenantIdsFromCampaign(Campaign campaign) {
        if(campaign == null){
            log.error("Campaign input is null");
            return Collections.emptyList();
        }
        List<Integer> covenantIds = new ArrayList<>();
        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM covenant WHERE campaign_name = ?")){
            statement.setString(1, campaign.getName());

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                covenantIds.add(resultSet.getInt("id")); //TODO
            }
        }catch (SQLException exp){
            log.error("Loading covenants from campaign {} failed with the following error: {}", campaign.getName(), exp.getMessage());
            return Collections.emptyList();
        }

        return covenantIds;
    }
}
