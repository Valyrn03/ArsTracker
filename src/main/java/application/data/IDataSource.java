package application.data;

import application.models.*;
import application.models.enums.AbilityCategory;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IDataSource {
    void close();

    public Connection getConnection();

    List<Ability> loadAbilitiesById(int id);

    boolean addAbility(int id, Ability ability);
}
