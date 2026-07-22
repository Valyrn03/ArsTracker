package application.data;

import application.models.Ability;
import application.models.ArsCharacter;
import application.models.CharacterFeature;
import application.models.Covenant;
import application.models.enums.AbilityCategory;
import application.models.enums.Attribute;
import application.models.enums.ExtraneousAttribute;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public class CharacterDataSource implements ICharacterDataSource{
    private IDataSource source;

    public CharacterDataSource(IDataSource src){
        this.source = src;
    }


    @Override
    public Optional<ArsCharacter> loadBaseCharacterFromId(int characterID){
        ResultSet resultSet = null;
        ResultSetMetaData metadata = null;
        Map<String, String> characterDetails = new HashMap<>();
        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE id = ?")){
            statement.setInt(1, characterID);

            resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.info("Character with id {} does not exist", characterID);
                resultSet.close();
                return Optional.empty();
            }

            metadata = resultSet.getMetaData();
            for(int i = 1; i < metadata.getColumnCount() + 1; i++){
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
            characterDetails = null;
        }

        if(characterDetails != null){
            switch(characterDetails.get("character_type")){
                case "0" -> {characterDetails.put("character_type", ArsCharacter.CharacterType.MAGUS.toString());}
                case "1" -> {characterDetails.put("character_type", ArsCharacter.CharacterType.COMPANION.toString());}
                case "2" -> {characterDetails.put("character_type", ArsCharacter.CharacterType.GROG.toString());}
            }
            return Optional.of(ArsCharacter.buildCharacterFromMap(characterDetails));
        }else{
            return Optional.empty();
        }
    }

    @Override
    public boolean updateCharacterCharacteristics(ArsCharacter character) {
        return false;
    }

    /*
    Query `applied_feature` table to get the features the given character has
        Then run loadFeatureFromId lol
     */
    @Override
    public List<CharacterFeature> loadFeaturesFromCharacter(ArsCharacter character) {
        if(character == null || character.getId() == 0){
            return Collections.emptyList();
        }
        List<Integer> featureIds = new ArrayList<>();

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT feature_id FROM applied_feature WHERE player_id = ?")){
            statement.setInt(1, character.getId());

            try(ResultSet resultSet = statement.executeQuery()){
                if(!resultSet.isBeforeFirst()){
                    log.info("Failed to find any features for character {}", character.getId());
                }
                do{
                    featureIds.add(resultSet.getInt(1));
                }while (resultSet.next());
            }
        }catch (SQLException exp){
            log.error("Failed to load features for character {} with error {}", character.getName(), exp.getMessage());
            return Collections.emptyList();
        }

        log.info("Loaded all applicable IDs: {}", featureIds);
        return featureIds.stream().map(this::loadFeatureFromId).filter(Optional::isPresent).map(Optional::get).toList();
    }

    @Override
    public Optional<CharacterFeature> loadFeatureFromId(int featureId) {
        if(featureId == 0){
            return Optional.empty();
        }

        CharacterFeature feature = null;
        try(Connection connection = source.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM feature WHERE id = ?")){
                statement.setInt(1, featureId);

                try(ResultSet resultSet = statement.executeQuery()){
                    feature = new CharacterFeature(
                            featureId,
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getInt("isVirtue") == 1,
                            resultSet.getInt("isMajor") == 1
                    );
                }
            }

            try(PreparedStatement statement = connection.prepareStatement("SELECT description FROM feature_rule WHERE feature_id = ?")){
                statement.setInt(1, featureId);

                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        feature.addRule(resultSet.getString("description"));
                    }
                }
            }
        }catch (SQLException exp){
            log.error("Failed to find feature {} due to error {}", featureId, exp.getMessage());
            return Optional.empty();
        }

        for(Ability ability : source.loadAbilitiesById(featureId)){
            feature.addAbility(ability);
        }

        log.info("returning {}", Optional.of(feature));
        return Optional.of(feature);
    }

    @Override
    public boolean addBaseCharacterToCovenant(Covenant covenant, ArsCharacter character) {
        if(covenant == null || character == null || covenant.getId() == 0){
            return false;
        }
        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO character (name, covenant_id, birth_season, character_type, intelligence, perception, strength, stamina, presence, communication, dexterity, quickness) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement idStatement = connection.prepareStatement("SELECT last_insert_rowid()")){
            statement.setString(1, character.getName());
            statement.setInt(2, covenant.getId());
            statement.setInt(3, character.getAttribute(ExtraneousAttribute.BIRTH_SEASON));
            statement.setInt(4, character.getCharacterType().id());

            statement.setInt(5, character.getAttribute(Attribute.INTELLIGENCE));
            statement.setInt(6, character.getAttribute(Attribute.PERCEPTION));
            statement.setInt(7, character.getAttribute(Attribute.STRENGTH));
            statement.setInt(8, character.getAttribute(Attribute.STAMINA));
            statement.setInt(9, character.getAttribute(Attribute.PRESENCE));
            statement.setInt(10, character.getAttribute(Attribute.COMMUNICATION));
            statement.setInt(11, character.getAttribute(Attribute.DEXTERITY));
            statement.setInt(12, character.getAttribute(Attribute.QUICKNESS));

            statement.execute();

            int newId = idStatement.executeQuery().getInt(1);
            log.info("New ID: {}", newId);
            character.setId(newId);
        }catch (SQLException exception){
            log.error("Failed to add character {} to covenant {} with error {}", character.getName(), covenant.getName(), exception.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean addFeatureToCharacter(ArsCharacter character, CharacterFeature feature) {
        if(character == null || loadBaseCharacterFromId(character.getId()).isEmpty()){
            log.info("Character has not been previously saved");
            return false;
        }
        if(feature == null || feature.getId() == 0 || loadFeatureFromId(feature.getId()).isEmpty()){
            log.info("Feature has not been previously saved");
            return false;
        }

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO applied_feature VALUES (?, ?)")){
            statement.setInt(1, character.getId());
            statement.setInt(2, feature.getId());
        }catch(SQLException exp){
            log.error("Failed to add feature {} to character {} with error {}", feature.getName(), character.getName(), exp.getMessage());
            return false;
        }

        log.info("Added feature {} to character {}", feature.getName(), character.getId());
        character.addFeature(feature);
        return true;
    }

    /*
    In order to save a new feature need to:
        Add it to "feature"
        For each ability, add it to ability_feature_rule
        For each rule, add it to feature_rule
     */
    @Override
    public boolean saveNewFeature(CharacterFeature feature) {
        if(feature == null){
            return false;
        }
        try(Connection connection = source.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO feature (name, description, isVirtue, isMajor) VALUES (?, ?, ?, ?)");
                PreparedStatement idStatement = connection.prepareStatement("SELECT last_insert_rowid()")){
                statement.setString(1, feature.getName());
                statement.setString(2, feature.getDescription());

                if(feature.getType().equals(CharacterFeature.FeatureType.VIRTUE)){
                    statement.setInt(3, 1);
                }else{
                    statement.setInt(3, 0);
                }

                if(feature.isMajor()){
                    statement.setInt(4, 1);
                }else{
                    statement.setInt(4, 0);
                }

                statement.execute();

                ResultSet resultSet = idStatement.executeQuery();
                feature.setId(resultSet.getInt(1));
            }catch (SQLException ex){
                log.error("Failed to add feature {} to table with error {}", feature.getName(), ex.getMessage());
                connection.rollback();
                if(feature.getId() != 0){
                    feature.setId(0);
                }
                throw ex;
            }
            log.info("Added feature {} to base table", feature.getName());

            for(String rule : feature.getRules()){
                try(PreparedStatement statement = connection.prepareStatement("INSERT INTO feature_rule VALUES (?, ?)")){
                    statement.setInt(1, feature.getId());
                    statement.setString(2, rule);

                    statement.execute();
                }catch (SQLException ex){
                    log.error("Failed to add rule to feature {} with error {}", feature.getName(), ex.getMessage());
                    connection.rollback();
                    throw ex;
                }
            }
            log.info("Added feature rules");

            connection.commit();
        }catch (SQLException exp){
            log.error("Failed to add feature with error {}", exp.getMessage());
            return false;
        }

        for(Ability ability : feature.getAbilities()){
            if(!source.addAbility(feature.getId(), ability)){
                return false;
            }
        }
        log.info("Added feature abilities");

        return true;
    }
}
