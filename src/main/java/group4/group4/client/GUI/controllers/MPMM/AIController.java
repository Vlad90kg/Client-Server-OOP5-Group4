package group4.group4.client.GUI.controllers.MPMM;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;

public class AIController implements Initializable {
    @FXML private Label imagesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String listOfImages = "";
        File dir = new File("images");
        File[] files = dir.listFiles();

        if (dir.list() != null && dir.list().length == 0) listOfImages = "There is currently no any images in the server";
        else for (int i = 0; i < files.length; i++) listOfImages += (i + 1) + ". " + files[i].getName() + "\n";

        imagesList.setText(listOfImages);
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) imagesList.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/imagesMenu.fxml")).load()));
        stage.show();
    }
}
