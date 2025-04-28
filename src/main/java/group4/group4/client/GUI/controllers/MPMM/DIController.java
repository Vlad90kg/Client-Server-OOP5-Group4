package group4.group4.client.GUI.controllers.MPMM;

import group4.group4.client.GUI.ConnectionManager;
import group4.group4.util.InputValidation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DIController {
    @FXML private TextField fileName;
    @FXML private Label message;

    @FXML
    protected void downloadFromServer() {
        String nameOfFile = fileName.getText();

        if (nameOfFile.trim().isEmpty()) {
            message.setText("Please specify file");
            return;
        }

        try {
            ConnectionManager.getInstance().getOut().println("getPhoneImage." + nameOfFile);
            String imageName = "images/" + nameOfFile;
            System.out.println(imageName);
            String imgResponse = ConnectionManager.getInstance().getIn().readLine();
            if (imgResponse.equals("Image not found")) message.setText("Specified image does not exist");
            if (!"READY".equals(imgResponse)) { throw new IOException("Unexpected response: " + imgResponse); }

            Socket dataSocket = new Socket(ConnectionManager.getInstance().getSocket().getInetAddress(), 8081);
            DataInputStream dataInputStream = new DataInputStream(dataSocket.getInputStream());
            long fileSize = dataInputStream.readLong();
            if (fileSize < 0 || fileSize > Integer.MAX_VALUE) { throw new IOException("Invalid file size: " + fileSize); }
            byte[] fileData = new byte[(int) fileSize];
            dataInputStream.readFully(fileData);

            try (FileOutputStream fileOutputStream = new FileOutputStream(imageName)) { fileOutputStream.write(fileData); }
            catch (IOException e) { System.out.println("Error writing file" + e.getMessage()); }
            message.setText("Successfully downloaded specified image");
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    protected void downloadAll() throws IOException {
        ConnectionManager.getInstance().getOut().println("getAllPhoneImages");
        String getAllImages = ConnectionManager.getInstance().getIn().readLine();
        System.out.println(getAllImages);
        if (getAllImages == null) {
            System.out.println("Response is null");
        } else if (!getAllImages.equals("READY")) {
            System.out.println("Unexpecte responce: " + getAllImages + "\nPlease try again later or contact admin to fix the problem\n");
        }
        try (Socket dataSock = new Socket(ConnectionManager.getInstance().getSocket().getInetAddress(), 8081);
             DataInputStream dis = new DataInputStream(dataSock.getInputStream())) {

            long zipLength = dis.readLong();
            System.out.println("ZIP file size: " + zipLength + " bytes");

            if (zipLength < 0 || zipLength > Integer.MAX_VALUE)
                throw new IOException("Invalid ZIP size: " + zipLength);

            byte[] zipBytes = new byte[(int) zipLength];
            dis.readFully(zipBytes);

            File zipFile = new File("downloadedImages.zip");
            try (FileOutputStream fos = new FileOutputStream(zipFile)) {
                fos.write(zipBytes);
            }
            unzipFile(zipFile, new File("images"));
        }

        message.setText("Successfully downloaded .zip archive consisting all images");
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
