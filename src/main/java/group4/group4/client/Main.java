package group4.group4.client;

import group4.group4.Exceptions.DaoException;

import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import group4.group4.util.InputValidation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws DaoException {
        Main mainInstance = new Main();
        mainInstance.start(8080);
    }

    public void start(int port) {
        try (Socket socket = new Socket("localhost", port);
             OutputStream outputStream = socket.getOutputStream();
        ) {
            PrintWriter out = new PrintWriter(outputStream, true);
            System.out.println("The client is running and has connected to the server.");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int id = 0, quantity = 0;
            String model = "", storage = "", chipset = "", input = "";
            double price = 0.0;
            boolean exit = false;
            boolean valid = false;
            while (!exit) {

                System.out.println("=== Mobile Phone Management System ===");
                System.out.println("1. Display All Phones");
                System.out.println("2. Search Phone by ID");
                System.out.println("3. Delete Phone by ID");
                System.out.println("4. Insert Phone");
                System.out.println("5. Update Phone");
                System.out.println("6. Filter Phones by ...");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");

                int choice = 0;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }
                System.out.println();
                switch (choice) {
                    case 1:
                        String getAllString = "getAll";
                        out.println(getAllString);
                        String response = in.readLine();
                        JSONArray jsonArray = new JSONArray(response);
                        List<MobilePhone> mobilePhones = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            mobilePhones.add(new MobilePhone(jsonObject));
                        }
                        for (MobilePhone mobilePhone : mobilePhones) {
                            System.out.println(mobilePhone);
                        }


                        break;
                    case 2:
                        int idToFind = -1;
                        boolean integerIsValid = true;

                        while (true) {
                            System.out.print("Enter ID of phone to find: ");
                            try {
                                idToFind = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                integerIsValid = false;
                            } finally {
                                if (integerIsValid) break;
                                else integerIsValid = true;
                            }
                        }

                        String req = "getById." + idToFind;
                        out.println(req);
                        String res = in.readLine();
                        System.out.println(res + "\n");
                        break;
                    case 3:
//                        mainInstance.deletePhoneById(daoMobilePhone);
                        break;
                    case 4:

                        System.out.println("Enter: ");
                        while (!valid) {
                            System.out.print("ID: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateInt(input);
                            if (!valid) continue;
                            id = Integer.parseInt(input);
                        }
                        valid = false;
                        while (!valid) {
                            System.out.print("Model: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateString(input);
                            if (!valid) continue;
                            model = input;
                        }
                        valid = false;
                        while (!valid) {
                            System.out.print("Quantity: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateInt(input);
                            if (valid)  quantity = Integer.parseInt(input);
                        }
                        valid = false;
                        while (!valid) {
                            System.out.print("Price: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateDouble(input);
                            if (valid) price = Double.parseDouble(input);

                        }
                        valid = false;
                        while (!valid) {
                            System.out.println("Add phone specifications: ");
                            System.out.print("Storage: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateString(input);
                            if (valid) storage = input;

                        }
                        valid = false;

                        while (!valid) {
                            System.out.print("Chipset: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateString(input);
                            if(valid) chipset = input;
                        }


                        JSONObject specificationsJson = new JSONObject();
                        JSONObject phonesJson = new JSONObject();
                        phonesJson.put("id", id);
                        phonesJson.put("model", model);
                        phonesJson.put("quantity", quantity);
                        phonesJson.put("price", price);
                        specificationsJson.put("phoneID", id);
                        specificationsJson.put("storage", storage);
                        specificationsJson.put("chipset", chipset);
                        JSONObject json = new JSONObject();
                        json.put("request", "insertPhone");
                        json.put("specifications", specificationsJson);
                        json.put("phones", phonesJson);
                        out.println(json);

//                        MobilePhone insertedPhone = mainInstance.insert(daoMobilePhone);
//                        System.out.println("Inserted Phone: " + insertedPhone);
                        break;
                    case 5:
//                        mainInstance.update(daoMobilePhone);
                        break;
                    case 6:
                        System.out.println("Please enter the threshold price: ");
                        try {
                            price = Double.parseDouble(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            continue;
                        }
                        List<MobilePhone> filteredMobilePhones = new ArrayList<>();
                        String findByFilter = "getByFilter." + price;
                        out.println(findByFilter);
                        String filterResult = in.readLine();
                        JSONArray getByFilterJson = new JSONArray(filterResult);
                        for (int i = 0; i < getByFilterJson.length(); i++) {
                            JSONObject jsonObject = getByFilterJson.getJSONObject(i);
                            System.out.println(jsonObject);
                            MobilePhone mobilePhone = new MobilePhone(jsonObject);
                            filteredMobilePhones.add(mobilePhone);
                        }
                        System.out.println(filteredMobilePhones);
                        break;
                    case 7:
                        exit = true;
                        out.println("exit");
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
