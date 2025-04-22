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

public class SPController {
    @FXML private final DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl();
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private Label brand, model, quantity, price, storage, chipset, message;
    @FXML private TextField idField;

    @FXML
    protected void find() {
        try {
            if (idField.getText().isEmpty()) {
                message.setText("Please specify ID");
                return;
            }

            MobilePhone foundPhone = dmpi.getById(Integer.parseInt(idField.getText()));

            if (foundPhone == null) {
                message.setText("Could not find any mobile phone with specified ID");
                return;
            }

            message.setText("");
            brand.setText("Brand: " + dbi.getById(foundPhone.getBrandId()).getName());
            model.setText("Model: " + foundPhone.getModel());
            quantity.setText("Quantity: " + foundPhone.getQuantity());
            price.setText("Price: $" + foundPhone.getPrice());
            storage.setText("Storage: " + foundPhone.getSpecifications().getStorage());
            chipset.setText("Chipset: " + foundPhone.getSpecifications().getChipset());
        }
        catch (NumberFormatException e) { message.setText("ID must be a number"); }
        catch (Exception e) { message.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) brand.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
