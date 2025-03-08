module group4.group4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens group4.group4 to javafx.fxml;
    exports group4.group4;
    exports group4.group4.GUI;
    opens group4.group4.GUI to javafx.fxml;
}