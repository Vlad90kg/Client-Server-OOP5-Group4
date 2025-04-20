module group.group {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.json;
    requires mysql.connector.java;

    opens group4.group4 to javafx.fxml;
    exports group4.group4.client.GUI;
    opens group4.group4.client.GUI to javafx.fxml;
    exports group4.group4.client;
    opens group4.group4.client to javafx.fxml;
}
