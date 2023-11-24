module Application {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
    requires java.net.http;
    requires java.sql;
    requires json.path;
    requires javafx.graphics;
    requires org.xerial.sqlitejdbc;
    requires org.json;
    requires freetts;
    requires java.desktop;
    opens Application to javafx.graphics;
    exports Application;
    exports DictionaryApplication.Controllers;
    opens DictionaryApplication.Controllers to javafx.fxml;
}