module application {
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    requires com.zaxxer.hikari;
    requires org.apache.commons.lang3;
    requires static lombok;
    requires org.jline;
    requires liquibase.core;
    requires org.apache.commons.io;

    exports application;
    exports application.terminal;
    exports application.models;
    exports application.commands;
    exports application.data;
    exports application.models.enums;
    exports application.commands.campaign;
    exports application.commands.covenant;
    exports application.commands.character;
}