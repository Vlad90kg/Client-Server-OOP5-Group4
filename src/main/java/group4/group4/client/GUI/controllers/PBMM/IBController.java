package group4.group4.client.GUI.controllers.PBMM;

import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.Brand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class IBController {
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private TextField nameField, descriptionField;
    @FXML private Label message;

    @FXML
    protected void insert() {
        try {
            if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
                message.setText("Fields cannot be empty");
                return;
            }

            dbi.insert(new Brand( nameField.getText(), descriptionField.getText()));
            message.setText("Successfully added new brand");
        }
        catch (Exception e) { message.setText("Unexpected error occurred. Please check fields"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
