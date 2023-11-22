module Application {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.gson;
    requires java.net.http;
    requires java.sql;
    requires json.path;
    requires javafx.graphics;
    opens Application to javafx.graphics;
    exports Application;
    exports DictionaryApplication.Controllers;
    opens DictionaryApplication.Controllers to javafx.fxml;
}