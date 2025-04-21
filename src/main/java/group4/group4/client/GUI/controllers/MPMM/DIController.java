package group4.group4.client.GUI.controllers.MPMM;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DIController {
    @FXML private Label titleText;

    @FXML
    protected void redirectTo(int option) throws IOException {
        FXMLLoader newScene = new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml"));
        if(option == 2) newScene = new FXMLLoader(getClass().getResource("/group4/group4/MPMMScenes/availableImages.fxml"));
        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.setScene(new Scene(newScene.load()));
        stage.show();
    }

    @FXML protected void goBack() throws IOException { redirectTo(1); }
    @FXML protected void goToList() throws IOException { redirectTo(2); }
}
