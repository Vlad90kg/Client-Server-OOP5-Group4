package group4.group4.client.GUI.controllers.PBMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.server.dto.Brand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DABController implements Initializable {
    @FXML private Label brandsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String brandsListString = "";
            ConnectionManager.getInstance().getOut().println("getAllBrand");
            String response = ConnectionManager.getInstance().getIn().readLine();
            JSONArray jsonArray = new JSONArray(response);
            List<Brand> brands = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                brands.add(new Brand(jsonObject));
            }

            if (brands.isEmpty()) brandsListString = "There is currently no any brands in the database";
            else for (Brand brand : brands) brandsListString += brand.getId() + ". " + brand.getName() + "\n";

            brandsList.setText(brandsListString);
        }
        catch (Exception e) { brandsList.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) brandsList.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
