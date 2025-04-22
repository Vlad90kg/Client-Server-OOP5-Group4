package group4.group4.client.GUI.controllers.PBMM;

import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dao.DaoMobilePhoneImpl;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PRTBController implements Initializable {
    @FXML private final DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl();
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private Label phonesList, brandName;
    @FXML private TextField brandField;

    @Override
    public void initialize(URL location, ResourceBundle resources) { filter(); }

    @FXML
    protected void filter() {
        try {
            String mobilePhonesList = "";
            List<MobilePhone> mobilePhones;

            if (brandField.getText().isEmpty()) {
                mobilePhones = dmpi.getAll();
                if (mobilePhones.isEmpty()) {
                    phonesList.setText("There is currently no any phones in the database");
                    return;
                }
            }
            else {
                mobilePhones = dmpi.getPhoneByBrand(Integer.parseInt(brandField.getText()));
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
        if(brandField.getText().isEmpty()) {
            brandName.setText("N/A");
            return;
        }

        Brand foundBrand = dbi.getById(Integer.parseInt(brandField.getText()));
        brandName.setText((foundBrand == null) ? "N/A" : foundBrand.getName());
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) phonesList.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
