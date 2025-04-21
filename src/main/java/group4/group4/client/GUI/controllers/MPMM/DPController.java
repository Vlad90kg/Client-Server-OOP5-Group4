package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.Exceptions.DaoException;
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
    @FXML private Label resultMessage;
    @FXML private TextField idField;

    @FXML
    protected void delete() {
        try {
            int result = dmpi.delete(Integer.parseInt(idField.getText()));
            resultMessage.setText((result == 1) ? "Successfully deleted phone with specified ID" : "Mobile phone with specified ID not found");
        }
        catch (DaoException e) { resultMessage.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        FXMLLoader newMenu = new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml"));
        Stage stage = (Stage) resultMessage.getScene().getWindow();
        stage.setScene(new Scene(newMenu.load()));
        stage.show();
    }
}
