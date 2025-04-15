package group4.group4.client;

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

            boolean exit = false;

            MobilePhoneMenu mobilePhoneMenu = new MobilePhoneMenu(scanner, socket, out, in);
            BrandMenu brandMenu = new BrandMenu(scanner, socket, out, in);
            while (!exit) {
                System.out.println("=== Mobile Phone Management System ===");
                System.out.println("1. Phones menu");
                System.out.println("2. Phone brands menu");
                System.out.println("3. Exit");
                System.out.print("Enter number to choose an option: ");

                int choice = 0;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }
                switch (choice) {
                    case 1:
                        mobilePhoneMenu.display();
                        break;
                    case 2:
                        brandMenu.display();
                        //
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
                mobilePhoneMenu.display();
            }

        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch (
                IOException e) {
            System.out.println("Cannot connect to server: " + e.getMessage());
        }
    }
}
