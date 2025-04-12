package group4.group4.client;

import group4.group4.Exceptions.DaoException;

import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            boolean exit = false;
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
                            int id = jsonObject.getInt("id");
                            int brandId = jsonObject.getInt("brand_id");
                            String model = jsonObject.getString("model");
                            int quantity = jsonObject.getInt("quantity");
                            double price = jsonObject.getDouble("price");

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
                            try { idToFind = Integer.parseInt(scanner.nextLine()); }
                            catch (NumberFormatException e) { integerIsValid = false; }
                            finally { if (integerIsValid) break; else integerIsValid = true; }
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
                        System.out.println("Enter : ");
//                        MobilePhone insertedPhone = mainInstance.insert(daoMobilePhone);
//                        System.out.println("Inserted Phone: " + insertedPhone);
                        break;
                    case 5:
//                        mainInstance.update(daoMobilePhone);
                        break;
                    case 6:
                        double price = 0.0;
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
