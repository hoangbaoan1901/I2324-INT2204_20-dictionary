module Application {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.net.http;
    requires java.sql;
    requires json.path;
    requires json.smart;
    requires javafx.graphics;
    requires org.xerial.sqlitejdbc;
    requires freetts;
    opens Application to javafx.graphics;
    exports Application;
    exports DictionaryApplication.Controllers;
    opens DictionaryApplication.Controllers to javafx.fxml;
}