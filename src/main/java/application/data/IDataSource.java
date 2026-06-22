package application.data;

import application.models.*;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IDataSource {
    void close();

    public Connection getConnection();
}
