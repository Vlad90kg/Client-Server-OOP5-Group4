package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.server.dao.DaoBrandImpl;
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
        try { resultMessage.setText((dmpi.delete(Integer.parseInt(idField.getText())) == 1) ? "Successfully deleted mobile phone" : "Could not find any mobile phone with specified ID"); }
        catch (Exception e) { resultMessage.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) resultMessage.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
