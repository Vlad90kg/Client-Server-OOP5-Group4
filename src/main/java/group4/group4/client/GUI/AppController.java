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
            case 12:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/MPMMScenes/searchPhone.fxml"));
                break;
            case 13:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/MPMMScenes/deletePhone.fxml"));
                break;
            case 14:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/MPMMScenes/insertPhone.fxml"));
                break;
            case 15:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/MPMMScenes/filterPhones.fxml"));
                break;
            case 16:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/menu/imagesMenu.fxml"));
                break;
            case 21:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/PBMMScenes/displayAllBrands.fxml"));
                break;
            case 22:
                newScene = new FXMLLoader(getClass().getResource("/group4/group4/PBMMScenes/searchBrand.fxml"));
                break;
            default:
                System.out.println("Invalid option passed to optionHandler() in AppController.java");
                break;
        }

        Stage stage = (Stage) titleText.getScene().getWindow();
        stage.setScene(new Scene(newScene.load()));
        stage.show();
    }

    // Main Menu
    @FXML protected void toMainMenu() throws IOException { optionHandler(0); }
    @FXML protected void toPhonesMenu() throws IOException { optionHandler(1); }
    @FXML protected void toBrandsMenu() throws IOException { optionHandler(2); }
    @FXML protected void exitButton() { Platform.exit(); }

    // Mobile Phones Management Menu
    @FXML protected void displayAllPhones() throws IOException { optionHandler(11); }
    @FXML protected void searchPhone() throws IOException { optionHandler(12); }
    @FXML protected void deletePhone() throws IOException { optionHandler(13); }
    @FXML protected void insertPhone() throws IOException { optionHandler(14); }
    @FXML protected void filterPhones() throws IOException { optionHandler(15); }
    @FXML protected void downloadImage() throws IOException { optionHandler(16); }

    // Phone Brands Management Menu
    @FXML protected void displayAllBrands() throws IOException { optionHandler(21); }
    @FXML protected void searchBrand() throws IOException { optionHandler(22); }
}
