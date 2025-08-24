module application {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.beryx.textio;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    requires com.zaxxer.hikari;

    opens application to javafx.fxml;
    exports application;
    exports application.controllers;
    opens application.controllers to javafx.fxml;
    exports application.displays;
    opens application.displays to javafx.fxml;
    exports application.utils;
    exports application.terminal;
    exports application.characters;
    exports application.commands;
}