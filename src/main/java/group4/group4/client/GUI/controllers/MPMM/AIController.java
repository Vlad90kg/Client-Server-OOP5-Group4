package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.client.GUI.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AIController implements Initializable {
    @FXML private VBox imagesContainer;
    @FXML private Label message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ConnectionManager.getInstance().getOut().println("getPhoneFileNames");
            String[] response = ConnectionManager.getInstance().getIn().readLine().split(",");

            if (response.length == 1 && response[0].isEmpty()) {
                message.setFont(new Font("Arial", 16));
                message.setText("There are currently no images on the server");
            }

            if (response.length > 0) {
                for (int i = 0; i < response.length; i++) {
                    VBox imageItem = new VBox(10);
                    String imageName = response[i];
                    imageItem.setStyle("-fx-background-color: #BDBDBD; -fx-padding: 25;");
                    imageItem.setAlignment(Pos.CENTER);

                    String imagePath = "images/" + imageName;
                    Image image = new Image("file:" + imagePath);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(300);
                    imageView.setFitHeight(300);

                    Label imageLabel = new Label(imageName);
                    imageLabel.setFont(new Font("Arial", 16));

                    imageItem.getChildren().addAll(imageView, imageLabel);
                    imagesContainer.getChildren().add(imageItem);
                }
            }
        }
        catch (Exception e) { message.setText("Unexpected error occurred"); }
    }

    @FXML
    protected void goBack() throws IOException {
        Stage stage = (Stage) message.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/group4/group4/menu/imagesMenu.fxml")).load()));
        stage.show();
    }
}
