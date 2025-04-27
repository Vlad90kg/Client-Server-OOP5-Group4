package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class DAPController implements Initializable {
    @FXML private Label phonesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String mobilePhonesList = "";
            ConnectionManager.getInstance().getOut().println("getAllPhone");
            String response = ConnectionManager.getInstance().getIn().readLine();
            JSONArray jsonArray = new JSONArray(response);
            List<MobilePhone> mobilePhones = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mobilePhones.add(new MobilePhone(jsonObject));
            }

            if (mobilePhones.isEmpty()) mobilePhonesList = "There is currently no any phones in the database";
            else for (MobilePhone phone : mobilePhones) {
                ConnectionManager.getInstance().getOut().println("getBrandById." + phone.getBrandId());
                String res = ConnectionManager.getInstance().getIn().readLine();
                JSONObject jsonObj = new JSONObject(res);
                Brand foundBrand = new Brand(jsonObj);
                String brand = foundBrand.getName();
                mobilePhonesList += phone.getId() + ". " + brand + " " + phone.getModel() + "\n";
            }

            phonesList.setText(mobilePhonesList);
        }
        catch (Exception e) { phonesList.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) phonesList.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")).load()));
        stage.show();
    }
}
