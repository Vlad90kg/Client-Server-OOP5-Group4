package group4.group4.client.GUI.controllers.PBMM;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.Brand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DBController {
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private Label resultMessage;
    @FXML private TextField idField;

    @FXML
    protected void delete() {
        try {
            if (idField.getText().isEmpty()) {
                resultMessage.setText("Please specify ID");
                return;
            }

            Brand brandToDelete = dbi.getById(Integer.parseInt(idField.getText()));

            if (brandToDelete == null) {
                resultMessage.setText("Could not find any brand with specified ID");
                return;
            }

            String deletedBrandName = brandToDelete.getName();
            dbi.delete(Integer.parseInt(idField.getText()));
            resultMessage.setText("Successfully deleted " + deletedBrandName);
        }
        catch (NumberFormatException e) { resultMessage.setText("ID must be a number"); }
        catch (Exception e) {
            String errorMessage = "Unexpected error occurred.";

            try {
                Brand brandToDelete = dbi.getById(Integer.parseInt(idField.getText()));
                if (brandToDelete != null) { errorMessage += "\nMake sure all " + brandToDelete.getName() + " phones are deleted first"; }
            } catch (DaoException ignored) {}

            resultMessage.setText(errorMessage);
        }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) resultMessage.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
