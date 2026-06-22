package application.data;

import application.models.Ability;
import application.models.ArsCharacter;
import application.models.CharacterFeature;
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
    public Optional<ArsCharacter> loadCharacterFromId(String characterID){
        ResultSet resultSet = null;
        ResultSetMetaData metadata = null;
        Map<String, String> characterDetails = new HashMap<>();
        try(Connection connection = source.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE id = ?")){
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
            characterDetails = null;
        }

        if(characterDetails != null){
            return Optional.of(ArsCharacter.buildCharacterFromMap(characterDetails, null)); //Need to remember why I passed in a Campaign object
        }else{
            return Optional.empty();
        }
    }

    @Override
    public boolean updateCharacter(ArsCharacter character) {
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
}
