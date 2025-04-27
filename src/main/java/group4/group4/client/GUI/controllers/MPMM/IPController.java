package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.server.JsonConverter;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class IPController {
    @FXML private TextField brandField, modelField, quantityField, priceField, storageField, chipsetField;
    @FXML private Label brandName, message;

    @FXML
    protected void insert() {
        try {
            if (
                    brandField.getText().isEmpty() ||
                    modelField.getText().isEmpty() ||
                    quantityField.getText().isEmpty() ||
                    priceField.getText().isEmpty() ||
                    storageField.getText().isEmpty() ||
                    chipsetField.getText().isEmpty()
            ) {
                message.setText("Fields cannot not be empty");
                return;
            }

            MobilePhone newPhone = new MobilePhone(
                    new Specifications(storageField.getText(), chipsetField.getText()),
                    Integer.parseInt(brandField.getText()),
                    modelField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    Double.parseDouble(priceField.getText())
            );

            JsonConverter jsonConverter = new JsonConverter();
            JSONObject phoneJson = jsonConverter.serializeMobilePhone(newPhone);
            JSONObject specJson = jsonConverter.serializeSpecifications(newPhone);
            JSONArray phoneSpecArray = new JSONArray();
            phoneSpecArray.put(phoneJson);
            phoneSpecArray.put(specJson);
            String jsonString = phoneSpecArray.toString();
            ConnectionManager.getInstance().getOut().println("insertPhone." + jsonString);
            ConnectionManager.getInstance().getIn().readLine();

            message.setText("Successfully added new mobile phone");
        }
        catch (NumberFormatException e) { message.setText("Brand ID, Quantity, or Price is not a number"); }
        catch (Exception e) { message.setText("Unexpected error occurred. Please check fields"); }
    }

    @FXML
    protected void handleBrandFieldUpdate() throws Exception {
        brandName.setText("N/A");

        if(brandField.getText().isEmpty()) {
            return;
        }

        try {
            Integer.parseInt(brandField.getText());
            ConnectionManager.getInstance().getOut().println("getBrandById." + brandField.getText());
            String res = ConnectionManager.getInstance().getIn().readLine();
            JSONObject jsonObj = new JSONObject(res);
            Brand foundBrand = new Brand(jsonObj);
            brandName.setText((foundBrand == null) ? "N/A" : foundBrand.getName());
        }
        catch (NumberFormatException e) { brandName.setText("N/A"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) brandField.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
