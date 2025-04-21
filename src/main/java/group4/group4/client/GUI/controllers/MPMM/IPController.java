package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import group4.group4.server.dao.DaoMobilePhoneImpl;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IPController {
    @FXML private final DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl();
    @FXML private TextField brandField, modelField, quantityField, priceField, storageField, chipsetField;

    @FXML
    protected void insert() {
        try {
            dmpi.insert(new MobilePhone(
                    new Specifications(storageField.getText(), chipsetField.getText()),
                    Integer.parseInt(brandField.getText()),
                    modelField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    Double.parseDouble(priceField.getText())
            ));
        }
        catch (Exception e) { brandField.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        FXMLLoader newMenu = new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml"));
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.setScene(new Scene(newMenu.load()));
        stage.show();
    }
}
