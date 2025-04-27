package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.client.GUI.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;

public class AIController implements Initializable {
    @FXML private Label imagesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ConnectionManager.getInstance().getOut().println("getPhoneFileNames");
            String listOfImages = "";
            String[] response = ConnectionManager.getInstance().getIn().readLine().split(",");

            if (response.length == 1 && response[0].isEmpty()) listOfImages = "There is currently no any images in the server";
            else for (int i = 0; i < response.length; i++) listOfImages += (i + 1) + ". " + response[i] + "\n";

            imagesList.setText(listOfImages);
        } catch (Exception e) { imagesList.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) imagesList.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/imagesMenu.fxml")).load()));
        stage.show();
    }
}
