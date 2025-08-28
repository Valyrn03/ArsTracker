package application.terminal;

import application.characters.Character;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    final static Logger logger = LoggerFactory.getLogger(DataSource.class);
    private static HikariConfig config = new HikariConfig(DataSource.class.getResource(".properties").toString());
    private static HikariDataSource source = new HikariDataSource(config);

    private DataSource(){

    }

    public static Connection getConnection(){
        try{
            return source.getConnection();
        }catch (SQLException exp){
            logger.warn(() -> "Failed to pull connection from pool: " + exp.toString());
            System.exit(1);
            return null;
        }
    }

    public static boolean add(Character character){
        return false;
    }

    public static ResultSet query(String query){
        Connection connection = DataSource.getConnection();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            connection.close();
            statement.close();
            return resultSet;
        } catch (SQLException e) {
            try{
                connection.close();
            } catch (SQLException ex) {
                logger.warn(() -> "Failed to close connection in query");
            }finally {
                logger.warn(() -> "Query has Failed");
            }
            return null;
        }
    }

    public static boolean post(String query){
        Connection connection = DataSource.getConnection();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            logger.warn(() -> "Update has Failed");
            throw new RuntimeException();
        }

        return true;
    }
}
