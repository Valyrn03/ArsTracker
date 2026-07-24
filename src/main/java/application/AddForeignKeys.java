package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Didn't realize this had to be done on the initial creation of tables, and Liquibase does not support adding foreign key constraints to SQLite DBs
 */
public class AddForeignKeys {
    public static void main(){
        List<String> foreign_keys = new ArrayList<>();

        foreign_keys.add("ALTER TABLE ");
    }
}
