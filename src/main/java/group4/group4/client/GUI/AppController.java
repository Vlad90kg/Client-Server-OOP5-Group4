package group4.group4.client.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AppController {
    // Variables
    @FXML private Label titleText;

    // Handler of selected options (clicked buttons) in all menu
    @FXML
    protected void optionHandler(int option) throws IOException {
        FXMLLoader newScene = new FXMLLoader(getClass().getResource("/group4/group4/main.fxml"));

        switch (option) {
            case 1:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml"));
                break;
            case 2:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml"));
                break;
            case 11:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/MPMMScenes/displayAllPhones.fxml"));
                break;
            default:
                System.out.println("Invalid option passed to optionHandler()");
                break;
        }

        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.setScene(new Scene(newScene.load()));
        stage.show();
    }

    // Main Menu
    @FXML
    protected void toMainMenu() throws IOException { optionHandler(0); }

    @FXML
    protected void toPhonesMenu() throws IOException { optionHandler(1); }

    @FXML
    protected void toBrandsMenu() throws IOException { optionHandler(2); }

    @FXML
    protected void exitButton() { Platform.exit(); }

    // Mobile Phones Management Menu
    @FXML
    protected void displayAllPhones() throws IOException { optionHandler(11); }

    // Phone Brands Management Menu
    // Code will be here in the future
}
