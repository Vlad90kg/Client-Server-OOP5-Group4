package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import group4.group4.server.dao.DaoMobilePhoneImpl;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IPController {
    @FXML private final DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl();
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private TextField brandField, modelField, quantityField, priceField, storageField, chipsetField;
    @FXML private Label brandName, message;

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

            message.setText("Successfully added new mobile phone");
        }
        catch (Exception e) { message.setText("Unexpected error occurred. Please check fields"); }
    }

    @FXML
    protected void handleBrandFieldUpdate() throws Exception {
        if(brandField.getText().isEmpty()) {
            brandName.setText("N/A");
            return;
        }

        Brand foundBrand = dbi.getById(Integer.parseInt(brandField.getText()));
        brandName.setText((foundBrand == null) ? "N/A" : foundBrand.getName());
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
