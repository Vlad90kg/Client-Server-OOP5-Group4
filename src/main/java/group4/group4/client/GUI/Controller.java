package group4.group4.client.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private Label titleText;

    @FXML
    protected void switchMenu(int which) throws IOException {
        FXMLLoader newMenu = new FXMLLoader(getClass().getResource("/group4/group4/main.fxml"));

        if (which == 1) { newMenu = new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml")); }
        else if (which == 2) { newMenu = new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")); }

        Parent root = newMenu.load();
        Stage stage = (Stage) titleText.getScene().getWindow();
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.show();
    }

    @FXML
    protected void toMainMenu() throws IOException { switchMenu(0); }

    @FXML
    protected void toPhonesMenu() throws IOException { switchMenu(1); }

    @FXML
    protected void toBrandsMenu() throws IOException { switchMenu(2); }

    @FXML
    protected void exitButton() { Platform.exit(); }
}
