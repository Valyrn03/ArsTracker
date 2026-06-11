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
    requires org.apache.commons.lang3;
    requires static lombok;

    opens application to javafx.fxml;
    exports application;
    exports application.gui.controllers;
    opens application.gui.controllers to javafx.fxml;
    exports application.gui.displays;
    opens application.gui.displays to javafx.fxml;
    exports application.utils;
    exports application.terminal;
    exports application.models;
    exports application.commands;
    exports application.database;
    opens application.database to javafx.fxml;
}