package application.terminal;

import application.characters.Character;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DataSource {
    private static HikariConfig config = new HikariConfig(DataSource.class.getResource(".properties").toString());
    private static HikariDataSource source = new HikariDataSource(config);

    private DataSource(){}

    public static Connection getConnection(){
        try{
            return source.getConnection();
        }catch (SQLException exp){
            log.warn("Failed to pull connection from pool: {}", exp.getMessage());
            System.exit(1);
            return null;
        }
    }

    public static boolean add(Character character){
        return false;
    }

    public static ResultSet query(String query){
        Connection connection = DataSource.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            connection.close();
            statement.close();
            return resultSet;
        } catch (SQLException e) {
            try{
                if (resultSet != null){
                    resultSet.close();
                }
                if(statement != null){
                    statement.close();
                }
                connection.close();
            } catch (SQLException exp) {
                log.warn("Failed to close connection: {}", exp.getMessage());
            }finally {
                log.warn("Query ({}) has Failed", query);
                resultSet = null;
                statement = null;
                connection = null;
            }
        }
        return null;
    }

    public static boolean post(String query){
        Connection connection = DataSource.getConnection();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            statement.close();
            resultSet.close();
        } catch (SQLException exp) {
            log.warn("Update has Failed: {}", exp.getMessage());
            throw new RuntimeException();
        }

        return true;
    }
}
