package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.client.GUI.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DIController {
    @FXML private TextField fileName;
    @FXML private Label message;

    @FXML
    protected void downloadFromServer() throws IOException {
        String imageName = fileName.getText();

        if (imageName.trim().isEmpty()) {
            message.setText("Please specify file");
            return;
        }

        ConnectionManager.getInstance().getOut().println("getPhoneImage." + imageName);

        if (ConnectionManager.getInstance().getIn().readLine().equals("Image not found")) message.setText("Could not find specified image");
        else {
            DataInputStream dataInputStream = new DataInputStream(ConnectionManager.getInstance().getSocket().getInputStream());
            long fileSize = dataInputStream.readLong();

            byte[] fileData = new byte[(int) fileSize];
            dataInputStream.readFully(fileData);

            try (FileOutputStream fileOutputStream = new FileOutputStream(imageName)) {
                fileOutputStream.write(fileData);
                message.setText("Successfully downloaded specified image");
            }
            catch (IOException e) { System.out.println("Error writing file" + e.getMessage()); }
        }
    }

    @FXML
    protected void downloadAll() throws IOException {
        ConnectionManager.getInstance().getOut().println("getAllPhoneImages");
        if (ConnectionManager.getInstance().getIn().readLine() == null) {
            message.setText("There is currently no any images in the server");
        }
        else {
            DataInputStream dis = new DataInputStream(ConnectionManager.getInstance().getSocket().getInputStream());
            long zipLength = dis.readLong();
            System.out.println("ZIP file size: " + zipLength + " bytes");

            byte[] zipBytes = new byte[(int) zipLength];
            dis.readFully(zipBytes);

            File zipFile = new File("downloadedImages.zip");
            try (FileOutputStream fos = new FileOutputStream(zipFile)) {
                fos.write(zipBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("ZIP file received: " + zipFile.getAbsolutePath());
            unzipFile(zipFile, new File("images"));
        }

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
    private void unzipFile(File downloadZipFile, File extractTo) {
        try (FileInputStream fileInputStream = new FileInputStream(downloadZipFile);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream)) {
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File outFile = new File(extractTo, zipEntry.getName());
                System.out.println("Extracting file: " + outFile.getAbsolutePath());

                try (FileOutputStream fos = new FileOutputStream(outFile);
                     BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) bos.write(buffer, 0, bytesRead);
                }
                zipInputStream.closeEntry();
            }

            System.out.println("File extracted to: " + extractTo.getAbsolutePath());
        }
        catch (FileNotFoundException e) { System.out.println("File not found: " + e.getMessage()); }
        catch (IOException e) { System.out.println("Cannot unzip due to connsection failure " + e.getMessage()); }
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
