package group4.group4.client.GUI.controllers.MPMM;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DIController {
    @FXML private TextField fileName;
    @FXML private Label message;

    @FXML
    protected void downloadFromServer() {
        File file = new File("images/" + fileName.getText().trim());

        if (fileName.getText().trim().isEmpty()) {
            message.setText("Please specify file");
            return;
        }
        else if (!file.exists() || !file.isFile()) {
            message.setText("Could not find specified image");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) baos.write(buffer, 0, bytesRead);

            byte[] receivedData = baos.toByteArray();
            String destPath = "downloads/" + fileName.getText().trim();
            File destFile = new File(destPath);

            destFile.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(destFile)) {
                fos.write(receivedData);
                message.setText("Successfully downloaded specified image");
            }
        }
        catch (IOException e) { System.out.println("Error during file transfer: " + e.getMessage()); }
    }

    @FXML
    protected void downloadAll() {
        String sourceDirPath = "images/";
        File sourceDir = new File(sourceDirPath);
        File[] files = sourceDir.listFiles();
        String zipFileName = "downloads/images.zip";
        File zipFile = new File(zipFileName);

        if (sourceDir.list() != null && sourceDir.list().length == 0) message.setText("There is currently no any images in the server");

        File downloadsDir = new File("downloads");
        if (!downloadsDir.exists()) downloadsDir.mkdirs();

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file);
                     BufferedInputStream bis = new BufferedInputStream(fis)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) zos.write(buffer, 0, bytesRead);
                    zos.closeEntry();

                    message.setText("Successfully downloaded .zip archive consisting all images");
                }
                catch (IOException e) { System.out.println("Error processing file " + file.getName() + ": " + e.getMessage()); }
            }
        }
        catch (IOException e) { System.out.println("Error creating zip file: " + e.getMessage()); }
    }

    @FXML
    protected void redirectTo(int option) throws IOException {
        FXMLLoader newScene = new FXMLLoader(getClass().getResource("/group4/group4/menu/phonesMenu.fxml"));
        if(option == 2) newScene = new FXMLLoader(getClass().getResource("/group4/group4/MPMMScenes/availableImages.fxml"));
        Stage stage = (Stage) fileName.getScene().getWindow();
        stage.setScene(new Scene(newScene.load()));
        stage.show();
    }

    @FXML protected void goBack() throws IOException { redirectTo(1); }
    @FXML protected void goToList() throws IOException { redirectTo(2); }
}
