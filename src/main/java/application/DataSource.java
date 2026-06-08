package application;

import application.models.Character;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
}
