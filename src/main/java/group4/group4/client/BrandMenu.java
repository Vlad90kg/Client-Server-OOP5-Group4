package group4.group4.client;

import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BrandMenu {
    private Scanner scanner;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public BrandMenu(Scanner scanner, Socket socket, PrintWriter out, BufferedReader in) {
        this.scanner = scanner;
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    public void display() {
        try {
            boolean exit = false;
            boolean valid = false;
            String response;
            while (!exit) {
                System.out.println("=== Phone Brands Management Menu ===");
                System.out.println("1. Display All Brands");
                System.out.println("2. Search Brand by ID");
                System.out.println("3. Delete Brand by ID");
                System.out.println("4. Insert Brand");
                System.out.println("5. Ch");
                System.out.println("6. Filter Brand by ...:");
                System.out.println("7. Back to main menu");

                System.out.print("Enter your choice: ");
                int choice = 0;

                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        String getAllString = "getAllBrand";
                        out.println(getAllString);
                        response = in.readLine();
                        JSONArray jsonArray = new JSONArray(response);
                        List<Brand> brands = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            System.out.println(jsonObject.toString());
                            brands.add(new Brand(jsonObject));
                        }
                        for (Brand b : brands) {
                            System.out.println(b);
                        }
                        break;
                    case 2:
                        int idToFind = -1;
                        boolean integerIsValid = true;

                        while (true) {
                            System.out.print("Enter ID of brand to find: ");
                            try {
                                idToFind = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                integerIsValid = false;
                            } finally {
                                if (integerIsValid) break;
                                else integerIsValid = true;
                            }
                        }

                        String req = "getBrandById." + idToFind;
                        out.println(req);
                        String res = in.readLine();
                        System.out.println(res + "\n");
                        break;
                    case 3:
                        break;
                    case 4:

                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        exit = true;
                        break;
                    default:
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
