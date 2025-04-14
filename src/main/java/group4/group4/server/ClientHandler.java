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

                    switch (inputLine) {
                        case "getAll":
                            String getAllString = jsonConverter.phonesListJson(getAllPhones(daoMobilePhone));
                            out.println(getAllString);
                            break;
                        case "getById":
                            String getByIdString = jsonConverter.phoneToJson(getPhoneById(daoMobilePhone, intArgument));
                            out.println(getByIdString);
                            break;
                        case "3":
//                        mainInstance.deletePhoneById(daoMobilePhone);
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
                                StringBuilder stringBuilder = new StringBuilder();

                                for (File file : files) {
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

    private void sendFiles(){
        try(OutputStream os = clientSocket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
            ZipOutputStream zos = new ZipOutputStream(bos);) {
            File[] files = new File("images/").listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        System.out.println("Adding file: " + file.getName());
                        try (FileInputStream fileInputStream = new FileInputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(fileInputStream);) {
                            ZipEntry zipEntry = new ZipEntry(file.getName());
                            zos.putNextEntry(zipEntry);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = bis.read(buffer)) != -1) {
                                zos.write(buffer, 0, bytesRead);
                            }
                            zos.closeEntry();
                        }
                    }
                }
            }

            zos.close();
            System.out.println("Zip complete.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //    public void deletePhoneById(DaoMobilePhone daoMobilePhone) throws DaoException {
//        // Feature 3
//        System.out.println("Enter ID to delete: ");
//        boolean validId = false;
//        int id = -1;
//
//        while (!validId) {
//            try {
//                id = Integer.parseInt(scanner.nextLine());
//                if (id >= 0) {
//                    validId = true;
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input. Please enter a number.");
//            }
//            System.out.println("Enter valid ID to delete: ");
//        }
//        scanner.nextLine();
//        int rowsAffected = daoMobilePhone.delete(id);
//        if (rowsAffected > 0) {
//            System.out.println("Phone with ID " + id + " deleted successfully!");
//        }
//        else {
//            System.out.println("No phone found with ID " + id);
//        }
//    }
//


    //
//    public void update(DaoMobilePhone daoMobilePhone) throws DaoException {
//        // Feature 5
//        String updatedModel = "";
//        int idToUpdate = 0, updatedBrandId = 0, updatedQuantity = 0;
//        double updatedPrice = 0;
//
//        boolean validInput = false;
//        while (!validInput) {
//            try {
//                System.out.print("Enter ID of mobile phone you would like to change: ");
//                idToUpdate = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter new brand ID: ");
//                updatedBrandId = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter new model: ");
//                updatedModel = scanner.nextLine();
//                System.out.print("Enter new quantity: ");
//                updatedQuantity = Integer.parseInt(scanner.nextLine());
//                System.out.print("Enter new price: ");
//                updatedPrice = Double.parseDouble(scanner.nextLine());
//
//                if (idToUpdate >= 0 || updatedBrandId > 0 || !updatedModel.isEmpty() || updatedQuantity >= 0 || updatedPrice > 0) {
//                    validInput = true;
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input. Please enter a number.");
//            }
//            if (!validInput) {
//                System.out.println("Invalid input. Please try again.");
//            }
//        }
//
//        int rowsAffected = daoMobilePhone.update(idToUpdate, new MobilePhone(updatedBrandId, updatedModel, updatedQuantity, updatedPrice));
//        if (rowsAffected > 0) {
//            System.out.println("Phone with ID " + idToUpdate + " updated successfully!");
//        }
//        else {
//            System.out.println("No phone found with ID " + idToUpdate);
//        }
//
//    }
//
//
    public List<MobilePhone> getFilteredPhones(DaoMobilePhone daoMobilePhone, double treshold) throws DaoException {
        // Feature 6
        Comparator<MobilePhone> comparator = (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice());

        return daoMobilePhone.findByFilter(comparator, treshold);
    }
}
