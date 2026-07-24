package application.data;

import application.models.*;
import application.models.enums.AbilityCategory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

import static application.models.Ability.*;

@Slf4j
public class MockDataSource implements IDataSource{
    private final DataSource dataSource;

    public MockDataSource(){
//        config = new HikariConfig(String.valueOf(DataSource.class.getResource("properties")));
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.sqlite.SQLiteDataSource");
        props.setProperty("dataSource.databaseName", "prodDB");
        props.setProperty("jdbcUrl", "jdbc:sqlite:");
        props.setProperty("maximumPoolSize", "1");

        dataSource = new DataSource(props);
    }

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public Connection getConnection() {
        return dataSource.getConnection();
    }

    @Override
    public List<Ability> loadAbilitiesById(int id) {
        return dataSource.loadAbilitiesById(id);
    }

    @Override
    public boolean addAbility(int id, Ability ability) {
        return dataSource.addAbility(id, ability);
    }
}
