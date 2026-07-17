package application.data;

import application.models.Ability;
import application.models.ArsCharacter;
import application.models.CharacterFeature;
import application.models.Covenant;
import application.models.enums.AbilityCategory;
import application.models.enums.Attribute;
import application.models.enums.ExtraneousAttribute;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            for(int i = 1; i < metadata.getColumnCount(); i++){
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
    Load from the `ability_tracker` table to get which type of ability and the specific instance
        `ability_category` stores which ability, as read from the list of abilities in the rulebook
        `ability` stores a specific instance, for example if there's multiple options
     */
    @Override
    public List<Ability> loadAbilitiesFromCharacter(ArsCharacter character) {
        return List.of();
    }

    /*
    Query `applied_feature` table to get the features the given character has
        Then the `feature` table to get the important information
        And `feature_rule` or `ability_feature_rule` to get how it specifically effects the character
            Numerically
     */
    @Override
    public List<CharacterFeature> loadFeaturesFromCharacter(ArsCharacter character) {
        return List.of();
    }

    @Override
    public Optional<CharacterFeature> loadFeatureFromId(int featureId) {
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

            //TODO handling categorical abilities here <- maybe instead of an FK to ability_category an FK to the ability table itself?
            try(PreparedStatement statement = connection.prepareStatement("SELECT ability, value FROM ability_feature_rule WHERE feature_id = ?")){
                statement.setInt(1, featureId);

                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        AbilityCategory category = AbilityCategory.valueOf(resultSet.getString("ability"));
                        feature.addAbility(new Ability(category, null, resultSet.getInt("value")));
                    }
                }
            }
        }catch (SQLException exp){
            log.error("Failed to find feature {} due to error {}", featureId, exp.getMessage());
            feature = null;
        }

        return Optional.ofNullable(feature);
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
            character.setId(newId);
        }catch (SQLException exception){
            log.error("Failed to add character {} to covenant {} with error {}", character.getName(), covenant.getName(), exception.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean addAbilityToCharacter(ArsCharacter character, Ability ability) {
        return false;
    }

    @Override
    public boolean addFeatureToCharacter(ArsCharacter character, CharacterFeature feature) {
        return false;
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

            for(Ability ability : feature.getAbilities()){
                try(PreparedStatement statement = connection.prepareStatement("INSERT INTO ability_feature_rule VALUES (?, ?, ?)")){
                    statement.setInt(1, feature.getId());
                    statement.setString(2, ability.toString());
                    statement.setInt(3, ability.getExperience());

                    statement.execute();
                }catch (SQLException ex){
                    log.error("Failed to add ability {} to feature {} with error {}", ability.toString(), feature.getName(), ex.getMessage());
                    connection.rollback();
                    throw ex;
                }
            }
            log.info("Added feature abilities");

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

        return true;
    }
}
