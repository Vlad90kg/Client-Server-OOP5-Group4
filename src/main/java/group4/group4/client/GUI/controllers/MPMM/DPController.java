package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.MobilePhone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

import group4.group4.server.dao.DaoMobilePhoneImpl;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DPController {
    @FXML private final DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl();
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

            MobilePhone phoneToDelete = dmpi.getById(Integer.parseInt(idField.getText()));

            if (phoneToDelete == null) {
                resultMessage.setText("Could not find any mobile phone with specified ID");
                return;
            }

            String deletedPhoneName = dbi.getById(phoneToDelete.getBrandId()).getName() + " " + phoneToDelete.getModel();
            dmpi.delete(Integer.parseInt(idField.getText()));
            resultMessage.setText("Successfully deleted " + deletedPhoneName);
        }
        catch (NumberFormatException e) { resultMessage.setText("ID must be a number"); }
        catch (Exception e) { resultMessage.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) resultMessage.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
