package application.data;

import application.models.Ability;
import application.models.ArsCharacter;
import application.models.CharacterFeature;
import application.models.Covenant;
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
        return Optional.empty();
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
        For each ability, add a ability_feature_rule
        For each
     */
    @Override
    public boolean saveNewFeature(CharacterFeature feature) {
        return false;
    }
}
