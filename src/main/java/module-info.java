module my.dictionary.my.dictionary.main {
    requires javafx.controls;
    requires java.sql;
    requires sqlite.jdbc;
    requires java.desktop;
    requires org.eclipse.collections.api;
    requires org.eclipse.collections.impl;
    requires javafx.fxml;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.google.common;
    //requires transitive javafx.graphics;

    //exports zw.home;
    opens zw.home to javafx.graphics;
}