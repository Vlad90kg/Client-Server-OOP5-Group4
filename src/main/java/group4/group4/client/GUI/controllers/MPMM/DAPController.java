package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.MobilePhone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import group4.group4.server.dao.DaoMobilePhoneImpl;
import javafx.stage.Stage;

public class DAPController implements Initializable {
    @FXML private final DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl();
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private Label phonesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String mobilePhonesList = "";
            List<MobilePhone> mobilePhones = dmpi.getAll();
            for (MobilePhone phone : mobilePhones) mobilePhonesList += phone.getId() + ". " + dbi.getById(phone.getBrandId()).getName() + " " + phone.getModel() + "\n";
            phonesList.setText(mobilePhonesList);
        }
        catch (DaoException e) { phonesList.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        FXMLLoader newMenu = new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml"));
        Stage stage = (Stage) phonesList.getScene().getWindow();
        stage.setScene(new Scene(newMenu.load()));
        stage.show();
    }
}
