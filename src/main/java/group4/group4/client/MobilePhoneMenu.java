package group4.group4.client;

import group4.group4.server.JsonConverter;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import group4.group4.util.InputValidation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MobilePhoneMenu {
    private Scanner scanner;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public MobilePhoneMenu(Scanner scanner, Socket socket, PrintWriter out, BufferedReader in) {
        this.scanner = scanner;
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    public void display() {
        try {
            int id = 0, quantity = 0, brand_id = 0;
            String model = "", storage = "", chipset = "", input = "", response = "";
            double price = 0.0;
            boolean exit = false;
            boolean valid = false;
            MobilePhone mobilePhone = null;
            while (!exit) {

                System.out.println("=== Mobile Phone Management Menu ===");
                System.out.println("1. Display All Phones");
                System.out.println("2. Search Phone by ID");
                System.out.println("3. Delete Phone by ID");
                System.out.println("4. Insert Phone");
                System.out.println("5. Filter Phones by price:");
                System.out.println("6. Download image from server");
                System.out.println("7. Back to main menu");
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
                        getAllOption(response);
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

                        String req = "getByPhoneId." + idToFind;
                        out.println(req);
                        String res = in.readLine();
                        if(res == null) {
                            System.out.println("Response is null");
                        }else{
                            System.out.println(res + "\n");
                        }
                        break;
                    case 3:
                        int idToDelete = -1;
                        valid = false;

                        while (!valid) {
                            System.out.print("Enter ID of phone to delete: ");
                            input = scanner.nextLine();
                            valid = InputValidation.validateInt(input);
                            if (!valid) continue;
                            idToDelete = Integer.parseInt(input);
                        }

                        out.println("deleteByPhoneId." + idToDelete);
                        System.out.println((Integer.parseInt(in.readLine()) == 1) ? "Successfully deleted phone with specified ID!\n" : "No phone with specified ID so nothing has been deleted\n");
                        break;

                    case 4:
                        insertMobilePhone(mobilePhone,valid, response, input, brand_id, model, quantity, price, storage, chipset);
                        break;

                    case 5:
                        filterMobilePhone(mobilePhone,valid,response,price);

                        break;
                    case 6:
                        imageSubmenu(valid, input);
                        break;

                    case 7:
                        exit = true;
                        out.println("exitPhone");
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException ioException){
            System.out.println("Connection issues" + ioException.getMessage());
        }
    }

    private void insertMobilePhone(MobilePhone mobilePhone, boolean valid, String response, String input, int brand_id, String model, int quantity, double price, String storage, String chipset) {
        try{
            System.out.println("Enter: ");
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
            if (response == null) {
                System.out.println("Response is null");
            }else if (response.equals("Brand not found")) {
                System.out.println("Brand not found");
            } else {
                mobilePhone = new MobilePhone(new JSONObject(response));
                System.out.println(mobilePhone);
            }
        }catch (IOException e){
            System.out.println("Connection issues" + e.getMessage());
        }



    }

    private  void  getAllOption(String response){
        try {
            String getAllString = "getAllPhone";
            out.println(getAllString);
            response = in.readLine();
            if(response == null) {
                System.out.println("response is null");
            } else {
                JSONArray jsonArray = new JSONArray(response);
                List<MobilePhone> mobilePhones = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    mobilePhones.add(new MobilePhone(jsonObject));
                }
                for (MobilePhone mp : mobilePhones) {
                    System.out.println(mp);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection issues" + e.getMessage());
        }
    }

    private void filterMobilePhone(MobilePhone mobilePhone, boolean valid, String response , double price) {
        try {
            System.out.println("Please enter the threshold price: ");
            try {
                price = Double.parseDouble(scanner.nextLine());

                List<MobilePhone> filteredMobilePhones = new ArrayList<>();

                String findByFilter = "getPhoneByFilter." + price;
                out.println(findByFilter);
                String filterResult = in.readLine();
                if(filterResult == null) {
                    System.out.println("Response is null");
                }else {
                    JSONArray getByFilterJson = new JSONArray(filterResult);

                    for (int i = 0; i < getByFilterJson.length(); i++) {
                        JSONObject jsonObject = getByFilterJson.getJSONObject(i);
                        System.out.println(jsonObject);
                        mobilePhone = new MobilePhone(jsonObject);
                        filteredMobilePhones.add(mobilePhone);
                    }
                    System.out.println(filteredMobilePhones);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }

        }catch (IOException e){
            System.out.println("Connection issues" + e.getMessage());
        }
    }
    private void imageSubmenu(boolean valid, String input) {
        try {
            System.out.println("1. Display available images");
            System.out.println("2. Download image from server");
            System.out.println("3. Dowload all images");

            String optionInput = scanner.nextLine();
            valid = InputValidation.validateInt(optionInput);
            if (valid) {
                int option = Integer.parseInt(optionInput);
                switch (option) {
                    case 1:
                        String request = "getPhoneFileNames";
                        out.println(request);
                        String fileNames = in.readLine();
                        if (fileNames == null) {
                            System.out.println("Response is null");
                        }else {
                            for (String fileName : fileNames.split(",")) {
                                System.out.println(fileName);
                            }
                        }

                        break;
                    case 2:
                        System.out.println("Input image name: ");
                        input = scanner.nextLine();
                        valid = InputValidation.validateString(input);
                        if (valid) // Send the command "getImage.[imageName]" on your command channel
                            out.println("getPhoneImage." + input);
                        String imageName = "images/" + input;
                        System.out.println(imageName);
                        if(in.readLine() == null){
                            System.out.println("Response is null");
                        } else if (in.readLine().equals("Image not found")) {
                            System.out.println("Image not found");
                        } else {
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            long fileSize = dataInputStream.readLong();
                            System.out.println("File size: " + fileSize + " bytes");

                            byte[] fileData = new byte[(int) fileSize];
                            dataInputStream.readFully(fileData);

                            try (FileOutputStream fileOutputStream = new FileOutputStream(imageName)) {
                                fileOutputStream.write(fileData);
                            } catch (IOException e) {
                                System.out.println("Error writing file" + e.getMessage());
                            }
                            System.out.println("File is Received");
                        }
                        break;
                    case 3:
                        out.println("getAllPhoneImages");
                        if(in.readLine() == null){
                            System.out.println("Response is null");
                        } else {
                            DataInputStream dis = new DataInputStream(socket.getInputStream());
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

                        break;
                    default:
                        System.out.println("Invalid input. Please enter 1,2 or 3.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

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
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                }
                zipInputStream.closeEntry();
            }
            System.out.println("File extracted to: " + extractTo.getAbsolutePath());

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot unzip due to connsection failure " + e.getMessage());

        }

    }

}
