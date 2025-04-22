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

public class SBController {
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private Label name, description, message;
    @FXML private TextField idField;

    @FXML
    protected void find() {
        try {
            if (idField.getText().isEmpty()) {
                message.setText("Please specify ID");
                return;
            }

            Brand foundBrand = dbi.getById(Integer.parseInt(idField.getText()));

            if (foundBrand == null) {
                message.setText("Could not find any brand with specified ID");
                return;
            }

            message.setText("");
            name.setText("Brand: " + foundBrand.getName());
            description.setText("Description: " + foundBrand.getDescription());
        }
        catch (NumberFormatException e) { message.setText("ID must be a number"); }
        catch (Exception e) { message.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
