module com.example.MyTunes_light {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires java.naming;
    requires javafx.media;
    requires java.desktop;


    opens dk.easv.MyTunes to javafx.fxml;
    exports dk.easv.MyTunes;
    exports dk.easv.MyTunes.GUI.Controller;
    opens dk.easv.MyTunes.GUI.Controller to javafx.fxml;

    opens dk.easv.MyTunes.BE to javafx.base;
}