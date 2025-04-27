package group4.group4.client.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.UnknownHostException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            ConnectionManager.getInstance();

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/group4/group4/main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            stage.setResizable(false);
            stage.setTitle("Mobile Store");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/group4/group4/icon.png")));
            stage.setScene(scene);
            stage.show();
        }
        catch (UnknownHostException e) { System.out.println("Unknown host: " + e.getMessage()); }
        catch (IOException e) { System.out.println("Cannot connect to server: " + e.getMessage()); }
    }

    public static void main(String[] args) {
        launch();
    }
}
