package group4.group4.client;

import com.mysql.cj.xdevapi.JsonArray;
import group4.group4.Exceptions.DaoException;

import group4.group4.server.JsonConverter;
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
            int id = 0, quantity = 0, brand_id = 0;
            String model = "", storage = "", chipset = "", input, response;
            double price = 0.0;
            boolean exit = false;
            boolean valid = false;
            MobilePhone mobilePhone;
            while (!exit) {

                System.out.println("=== Mobile Phone Management System ===");
                System.out.println("1. Display All Phones");
                System.out.println("2. Search Phone by ID");
                System.out.println("3. Delete Phone by ID");
                System.out.println("4. Insert Phone");
                System.out.println("5. Update Phone");
                System.out.println("6. Filter Phones by price:");
                System.out.println("7. Download image from server");
                System.out.println("8. Exit");
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
                        response = in.readLine();
                        JSONArray jsonArray = new JSONArray(response);
                        List<MobilePhone> mobilePhones = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            mobilePhones.add(new MobilePhone(jsonObject));
                        }
                        for (MobilePhone mp : mobilePhones) {
                            System.out.println(mp);
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
                            System.out.print("Brand ID: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateInt(input);
                            if (valid) brand_id = Integer.parseInt(input);
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
                            if (valid) quantity = Integer.parseInt(input);
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
                            if (valid) chipset = input;
                        }

                        Specifications specifications = new Specifications(storage, chipset);
                        MobilePhone phoneToInsert = new MobilePhone(specifications, brand_id, model, quantity, price);
                        JsonConverter jsonConverter = new JsonConverter();
                        JSONObject phoneJson = jsonConverter.serializeMobilePhone(phoneToInsert);
                        JSONObject specJson = jsonConverter.serializeSpecifications(phoneToInsert);
                        JSONArray phoneSpecArray = new JSONArray();
                        phoneSpecArray.put(phoneJson);
                        phoneSpecArray.put(specJson);
                        System.out.println("array to send" + phoneSpecArray);
                        String jsonString = phoneSpecArray.toString();
                        out.println("insertPhone." + jsonString);
                        response = in.readLine();
                        System.out.println(response);
                        mobilePhone = new MobilePhone(new JSONObject(response));
                        System.out.println(mobilePhone);
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
                            mobilePhone = new MobilePhone(jsonObject);
                            filteredMobilePhones.add(mobilePhone);
                        }
                        System.out.println(filteredMobilePhones);
                        break;
                    case 7:
//                        src/main/java/group4/group4/server/images/img.png   | Bin 0 -> 104674 bytes
//                        src/main/java/group4/group4/server/images/img_1.png | Bin 0 -> 120148 bytes
//                        src/main/java/group4/group4/server/images/img_2.png | Bin 0 -> 112529 bytes
//                        src/main/java/group4/group4/server/images/img_3.png | Bin 0 -> 136313 bytes
                        System.out.println("1. Display available images");
                        System.out.println("2. Download image from server");
                        System.out.println("3. Dowload all images");

                        String optionInput = scanner.nextLine();
                        valid = InputValidation.validateInt(optionInput);
                        if (valid) {
                            int option = Integer.parseInt(optionInput);
                            switch (option) {
                                case 1:
                                    String request = "getFileNames";
                                    out.println(request);
                                    String fileNames = in.readLine();
                                    for (String fileName : fileNames.split(",")) {
                                        System.out.println(fileName);
                                    }
                                    break;
                                case 2:
                                    System.out.println("Input image name: ");
                                    input = scanner.nextLine();
                                    valid = InputValidation.validateString(input);
                                    if (valid) out.println("getImage." + input);

                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Invalid input. Please enter 1,2 or 3.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                        }



                        break;
                    case 8:
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


    public void receiveFile() {

    }
}
