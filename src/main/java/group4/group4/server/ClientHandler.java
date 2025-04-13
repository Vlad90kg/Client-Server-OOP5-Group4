package group4.group4.server;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dao.DaoMobilePhoneImpl;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

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
            String inputLine, jsonString = "";

            boolean exit = false;
            while (!exit) {
                inputLine = in.readLine();
                if (inputLine == null) {
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
