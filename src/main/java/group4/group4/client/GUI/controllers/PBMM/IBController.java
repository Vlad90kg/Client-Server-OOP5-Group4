package group4.group4.client.GUI.controllers.PBMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.server.JsonConverter;
import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class IBController {
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private TextField nameField, descriptionField;
    @FXML private Label message;

    @FXML
    protected void insert() {
        try {
            if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
                message.setText("Fields cannot be empty");
                return;
            }

            Brand brandToInsert = new Brand(nameField.getText(), descriptionField.getText());
            JsonConverter jsonConverter = new JsonConverter();
            JSONObject brandJson = jsonConverter.serializeBrand(brandToInsert);
            JSONArray brandJsonArray = new JSONArray();
            brandJsonArray.put(brandJson);
            String jsonString = brandJsonArray.toString();
            ConnectionManager.getInstance().getOut().println("insertBrand." + jsonString);
            ConnectionManager.getInstance().getIn().readLine();

            message.setText("Successfully added new brand");
        }
        catch (Exception e) { message.setText("Unexpected error occurred. Please check fields"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
