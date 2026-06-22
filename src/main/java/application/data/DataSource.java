package application.data;

import application.models.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.*;
import java.util.*;

@Slf4j
public class DataSource implements IDataSource{
    private HikariConfig config;
    private HikariDataSource source;

    public DataSource(){
//        config = new HikariConfig(String.valueOf(DataSource.class.getResource("properties")));
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.sqlite.SQLiteDataSource");
        props.setProperty("dataSource.databaseName", "prodDB");
        props.setProperty("jdbcUrl", "jdbc:sqlite:database.db");

        config = new HikariConfig(props);
        source = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection(){
        try{
            return source.getConnection();
        }catch (SQLException exp){
            log.warn("Failed to pull connection from pool: {}", exp.getMessage());
            System.exit(1);
            return null;
        }
    }

    @Override
    public void close(){
        source.close();
    }
}
