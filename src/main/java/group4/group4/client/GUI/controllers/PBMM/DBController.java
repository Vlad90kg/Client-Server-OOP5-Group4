package group4.group4.client.GUI.controllers.PBMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    @FXML private final DaoBrandImpl dbi = new DaoBrandImpl();
    @FXML private Label resultMessage;
    @FXML private TextField idField;

    @FXML
    protected void delete() {
        try {
            String strId = idField.getText();
            List<MobilePhone> mobilePhones = new ArrayList<>();

            if (strId.isEmpty()) {
                resultMessage.setText("Please specify ID");
                return;
            }

            int intId = Integer.parseInt(strId);

            ConnectionManager.getInstance().getOut().println("getRelatedToBrandById." + intId);
            String response = ConnectionManager.getInstance().getIn().readLine();

                String[] arr = response.split("/");

                JSONObject jsonObject = new JSONObject(arr[0]);
                Brand brand1 = new Brand(jsonObject);
                System.out.println(brand1);
                JSONArray jsonArray = new JSONArray(arr[1]);

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    mobilePhones.add(new MobilePhone(jsonObject));
                }

            if (!mobilePhones.isEmpty()) {
                resultMessage.setText("Unable to delete brand. Delete all their mobile phones first");
                return;
            }

            ConnectionManager.getInstance().getOut().println("getBrandById." + intId);
            response = ConnectionManager.getInstance().getIn().readLine();
            jsonObject = new JSONObject(response);
            Brand brandToDelete = new Brand(jsonObject);

            String deletedBrandName = brandToDelete.getName();

            ConnectionManager.getInstance().getOut().println("deleteBrandById." + intId);
            ConnectionManager.getInstance().getIn().readLine();
            resultMessage.setText("Successfully deleted " + deletedBrandName);
        }
        catch (NumberFormatException e) { resultMessage.setText("ID must be a number"); }
        catch (RuntimeException e) { resultMessage.setText("Could not find any brand with specified ID"); }
        catch (Exception e) { resultMessage.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) resultMessage.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/brandsMenu.fxml")).load()));
        stage.show();
    }
}
