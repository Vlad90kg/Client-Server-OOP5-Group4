package group4.group4.server;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dao.DaoMobilePhoneImpl;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            DaoMobilePhone daoMobilePhone = new DaoMobilePhoneImpl();
            System.out.println("Client connected");
            JsonConverter jsonConverter = new JsonConverter();
            String inputLine, jsonString = "", imageName = "";
            FileInputStream fileInputStream;
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            boolean exit = false;
            while (!exit) {
                inputLine = in.readLine();
                if (inputLine == null || inputLine.isEmpty()) {
                    System.out.println("Client disconnected");
                } else {
                    int intArgument = 0;
                    double price = 0;
                    if (inputLine.startsWith("getById")) {
                        intArgument = Integer.parseInt(inputLine.substring(inputLine.indexOf('.') + 1));
                        inputLine = "getById";
                    } else if (inputLine.startsWith("deleteById")) {
                        intArgument = Integer.parseInt(inputLine.substring(inputLine.indexOf('.') + 1));
                        inputLine = "deleteById";
                    } else if (inputLine.startsWith("getByFilter")) {
                        price = Double.parseDouble(inputLine.substring(inputLine.indexOf('.') + 1));
                        inputLine = "getByFilter";
                    } else if (inputLine.startsWith("insertPhone")) {
                        jsonString = inputLine.substring(inputLine.indexOf('.') + 1);
                        inputLine = "insertPhone";
                    } else if (inputLine.startsWith("getImage")) {
                        imageName = inputLine.substring(inputLine.indexOf('.') + 1);
                        inputLine = "getImage";
                    }
                    System.out.println(inputLine+"<<<<<<");
                    switch (inputLine) {
                        case "getAll":
                            String getAllString = jsonConverter.phonesListJson(getAllPhones(daoMobilePhone));
                            out.println(getAllString);
                            break;
                        case "getById":
                            String getByIdString = jsonConverter.phoneToJson(getPhoneById(daoMobilePhone, intArgument));
                            out.println(getByIdString);
                            break;
                        case "deleteById":
                            int deletionResponse = daoMobilePhone.delete(intArgument);
                            out.println(deletionResponse);
                            break;
                        case "insertPhone":
                            JSONArray jsonArray = new JSONArray(jsonString);

                            MobilePhone toInsert = new MobilePhone(jsonArray.getJSONObject(0));
                            Specifications specifications = new Specifications(jsonArray.getJSONObject(1));
                            toInsert.setSpecifications(specifications);
                            String insertPhoneString = jsonConverter.phoneToJson(daoMobilePhone.insert(toInsert));
                            out.println(insertPhoneString);
                            break;
                        case "5":
//                        mainInstance.update(daoMobilePhone);
                            break;
                        case "getByFilter":
                            String findByFilter = jsonConverter.phonesListJson(getFilteredPhones(daoMobilePhone, price));
                            out.println(findByFilter);
                            break;
                        case "getFileNames":
                            File dir = new File("images");
                            File[] files = dir.listFiles();

                            if (files != null) {
                                System.out.println(files.length);
                                StringBuilder stringBuilder = new StringBuilder();

                                for (File file : files) {
                                    System.out.println(file.getName());
                                    stringBuilder.append(file.getName()).append(",");
                                }

                                String jsonStringImage = stringBuilder.toString();
                                out.println(jsonStringImage);
                            }
                            break;
                        case "getImage":
                            File dirImage = new File("images/" + imageName);
                            fileInputStream = new FileInputStream(dirImage);
                            dataOutputStream.writeLong(fileInputStream.available());

                            byte[] buffer = new byte[4096];

                            while (fileInputStream.read(buffer) != -1) {
                                dataOutputStream.write(buffer, 0, buffer.length);
                                dataOutputStream.flush();
                            }
                            fileInputStream.close();
                            break;

                        case "getAllImages":
                            sendFiles();
                            break;

                        case "exit":
                            exit = true;
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MobilePhone> getAllPhones(DaoMobilePhone daoMobilePhone) throws DaoException {
        // Feature 1
        List<MobilePhone> list = daoMobilePhone.getAll();
        for (MobilePhone mobilePhone : list) {
            System.out.println(mobilePhone);
        }
        return list;
    }

    public MobilePhone getPhoneById(DaoMobilePhone daoMobilePhone, int idToSearch) throws DaoException {
        // Feature 2
        MobilePhone phone = daoMobilePhone.getById(idToSearch);

        if (phone != null) {
            System.out.println("Id found:");
            System.out.println(phone);
        } else {
            System.out.println("No Phone found with ID: " + idToSearch);
        }
        return phone;
    }

    private void sendFiles() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            File[] files = new File("images/").listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        System.out.println("Adding file: " + file.getName());
                        try (FileInputStream fis = new FileInputStream(file)) {
                            zos.putNextEntry(new ZipEntry(file.getName()));
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                zos.write(buffer, 0, bytesRead);
                            }
                            zos.closeEntry();
                        }
                    }
                }
            }
            zos.finish();
        }
        byte[] zipBytes = baos.toByteArray();
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

        dos.writeLong(zipBytes.length);

        dos.write(zipBytes);
        dos.flush();
        System.out.println("getAllImages: ZIP sent, size: " + zipBytes.length);
    }


    public List<MobilePhone> getFilteredPhones(DaoMobilePhone daoMobilePhone, double treshold) throws DaoException {
        // Feature 6
        Comparator<MobilePhone> comparator = (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice());

        return daoMobilePhone.findByFilter(comparator, treshold);
    }
}
