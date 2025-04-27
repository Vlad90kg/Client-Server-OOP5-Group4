package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

public class SPController {
    @FXML private Label brand, model, quantity, price, message;
    @FXML private TextField idField;

    @FXML
    protected void find() {
        try {
            String strId = idField.getText();

            if (strId.isEmpty()) {
                message.setText("Please specify ID");
                return;
            }

            int intId = Integer.parseInt(strId);

            ConnectionManager.getInstance().getOut().println("getByPhoneId." + intId);
            String response = ConnectionManager.getInstance().getIn().readLine();
            JSONObject jsonObject = new JSONObject(response);
            MobilePhone foundPhone = new MobilePhone(jsonObject);

            ConnectionManager.getInstance().getOut().println("getBrandById." + foundPhone.getBrandId());
            String res = ConnectionManager.getInstance().getIn().readLine();
            JSONObject jsonObj = new JSONObject(res);
            Brand foundBrand = new Brand(jsonObj);

            message.setText("");
            brand.setText("Brand: " + foundBrand.getName());
            model.setText("Model: " + foundPhone.getModel());
            quantity.setText("Quantity: " + foundPhone.getQuantity());
            price.setText("Price: $" + foundPhone.getPrice());
        }
        catch (NumberFormatException e) { message.setText("ID must be a number"); }
        catch (RuntimeException e) { message.setText("Could not find any mobile phone with specified ID"); }
        catch (Exception e) { message.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) brand.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
