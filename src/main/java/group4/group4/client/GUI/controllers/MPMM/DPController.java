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

public class DPController {
    @FXML private Label resultMessage;
    @FXML private TextField idField;

    @FXML
    protected void delete() {
        try {
            String strId = idField.getText();

            if (strId.isEmpty()) {
                resultMessage.setText("Please specify ID");
                return;
            }

            int intId = Integer.parseInt(strId);
            ConnectionManager.getInstance().getOut().println("getByPhoneId." + intId);
            String response = ConnectionManager.getInstance().getIn().readLine();
            JSONObject jsonObject = new JSONObject(response);
            MobilePhone phoneToDelete = new MobilePhone(jsonObject);

            ConnectionManager.getInstance().getOut().println("getBrandById." + phoneToDelete.getBrandId());
            String res = ConnectionManager.getInstance().getIn().readLine();
            JSONObject jsonObj = new JSONObject(res);
            Brand foundBrand = new Brand(jsonObj);

            String deletedPhoneName = foundBrand.getName() + " " + phoneToDelete.getModel();

            ConnectionManager.getInstance().getOut().println("deleteByPhoneId." + intId);
            ConnectionManager.getInstance().getIn().readLine();
            resultMessage.setText("Successfully deleted " + deletedPhoneName);
        }
        catch (NumberFormatException e) { resultMessage.setText("ID must be a number"); }
        catch (RuntimeException e) { resultMessage.setText("Could not find any mobile phone with specified ID"); }
        catch (Exception e) { resultMessage.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) resultMessage.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
