package application.terminal;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sqlite.util.Logger;
import org.sqlite.util.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

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
            System.exit(0);
            return null;
        }
    }
}
