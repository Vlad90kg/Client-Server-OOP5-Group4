package group4.group4.client.GUI.controllers.PBMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PRTBController implements Initializable {
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private Label phonesList, brandName;
    @FXML private TextField brandField;

    @Override
    public void initialize(URL location, ResourceBundle resources) { filter(); }

    @FXML
    protected void filter() {
        try {
            String mobilePhonesList = "";
            List<MobilePhone> mobilePhones = new ArrayList<>();

            if (brandField.getText().isEmpty()) {
                ConnectionManager.getInstance().getOut().println("getAllPhone");
                String response = ConnectionManager.getInstance().getIn().readLine();
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mobilePhones.add(new MobilePhone(jsonObject));
                }

                if (mobilePhones.isEmpty()) {
                    phonesList.setText("There is currently no any phones in the database");
                    return;
                }
            }
            else {
                ConnectionManager.getInstance().getOut().println("getRelatedToBrandById." + Integer.parseInt(brandField.getText()));
                String response = ConnectionManager.getInstance().getIn().readLine();

                if(response.equals("Brand not found")) {
                    phonesList.setText("This brand does not exist");
                    return;
                }
                else {
                    String[] arr = response.split("/");

                    JSONObject jsonObject = new JSONObject(arr[0]);
                    Brand brand1 = new Brand(jsonObject);
                    System.out.println(brand1);
                    JSONArray jsonArray = new JSONArray(arr[1]);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        mobilePhones.add(new MobilePhone(jsonObject));
                    }
                }

                if (mobilePhones.isEmpty()) {
                    phonesList.setText("Currently this brand has no phones");
                    return;
                }
            }

            for (MobilePhone phone : mobilePhones) mobilePhonesList += phone.getId() + ". " + dbi.getById(phone.getBrandId()).getName() + " " + phone.getModel() + "\n";
            phonesList.setText(mobilePhonesList);
        }
        catch (NumberFormatException e) { phonesList.setText("Brand ID must be a number"); }
        catch (Exception e) { phonesList.setText("Unexpected error occurred"); }
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
        Stage stage = (Stage) phonesList.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
