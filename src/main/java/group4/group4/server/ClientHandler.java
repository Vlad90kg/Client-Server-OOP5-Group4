package group4.group4.server;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoBrand;
import group4.group4.server.dao.DaoBrandImpl;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dao.DaoMobilePhoneImpl;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
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
            System.out.println("Client connected");
            JsonConverter jsonConverter = new JsonConverter();
            String inputLine, jsonString = "";

            boolean exit = false;
            while (!exit) {
                inputLine = in.readLine();
                if (inputLine == null || inputLine.isEmpty()) {
                    System.out.println("Client disconnected");
                    exit = true;
                } else {

                    if (!inputLine.toLowerCase().contains("phone")) {

                        exit = brandHandler(inputLine, jsonConverter,jsonString, out,in);
                    } else {
                        exit = phoneHandler(inputLine, jsonConverter,jsonString, out,in);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected" + e.getMessage());

        } catch (DaoException e) {
            System.out.println("Database issues " + e.getMessage());
        }
    }

    private boolean phoneHandler(String inputLine, JsonConverter jsonConverter, String jsonString, PrintWriter out, BufferedReader in) throws DaoException {
            try{
                DaoMobilePhoneImpl daoMobilePhone = new DaoMobilePhoneImpl();
                String imageName = "";

                int intArgument = 0;
                double price = 0;
                if (inputLine.startsWith("getByPhoneId")) {
                    intArgument = Integer.parseInt(inputLine.substring(inputLine.indexOf('.') + 1));
                    inputLine = "getByPhoneId";
                } else if (inputLine.startsWith("deleteByPhoneId")) {
                    intArgument = Integer.parseInt(inputLine.substring(inputLine.indexOf('.') + 1));
                    inputLine = "deleteByPhoneId";
                } else if (inputLine.startsWith("getPhoneByFilter")) {
                    price = Double.parseDouble(inputLine.substring(inputLine.indexOf('.') + 1));
                    inputLine = "getPhoneByFilter";
                } else if (inputLine.startsWith("insertPhone")) {
                    jsonString = inputLine.substring(inputLine.indexOf('.') + 1);
                    inputLine = "insertPhone";
                } else if (inputLine.startsWith("getPhoneImage")) {
                    imageName = inputLine.substring(inputLine.indexOf('.') + 1);
                    inputLine = "getPhoneImage";
                }
                switch (inputLine) {
                    case "getAllPhone":
                        String getAllString = jsonConverter.phonesListJson(getAllPhones(daoMobilePhone));
                        out.println(getAllString);
                        break;
                    case "getByPhoneId":
                        String getByIdString = jsonConverter.phoneToJson(getPhoneById(daoMobilePhone, intArgument));
                        out.println(getByIdString);
                        break;
                    case "deleteByPhoneId":
                        int deletionResponse = daoMobilePhone.delete(intArgument);
                        out.println(deletionResponse);
                        break;
                    case "insertPhone":
                        JSONArray jsonArray = new JSONArray(jsonString);
                        int brand_id = jsonArray.getJSONObject(0).getInt("brand_id");
                        if(daoMobilePhone.existsById(brand_id)){
                            MobilePhone toInsert = new MobilePhone(jsonArray.getJSONObject(0));
                            Specifications specifications = new Specifications(jsonArray.getJSONObject(1));
                            toInsert.setSpecifications(specifications);
                            String insertPhoneString = jsonConverter.phoneToJson(daoMobilePhone.insert(toInsert));
                            out.println(insertPhoneString);
                        }else {
                            out.println("Brand not found");
                        }


                        break;
                    case "getPhoneByFilter":
                        String findByFilter = jsonConverter.phonesListJson(getFilteredPhones(daoMobilePhone, price));
                        out.println(findByFilter);
                        break;
                    case "getPhoneFileNames":
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
                    case "getPhoneImage":
                        File file = new File("images/" + imageName);
                        if(file.exists()){
                            try (FileInputStream fis = new FileInputStream(file);
                                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                                long fileSize = file.length();
                                System.out.println(fileSize);
                                dos.writeLong(fileSize);
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = bis.read(buffer)) != -1) {
                                    System.out.println(bytesRead);
                                    dos.write(buffer, 0, bytesRead);
                                }
                                dos.flush();
                            }
                        } else {
                            out.println("Image not found");
                        }
                        break;

                    case "getAllPhoneImages":
                        sendFiles();
                        break;

                    case "exitPhone":
                        System.out.println("Exiting...");
                        return true;
                    default:
                        System.out.println("Invalid choice. Please try again.");

                }

            } catch (FileNotFoundException e) {
                System.out.println("Exception " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database issues " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Connection issues " + e.getMessage());
            }
        return false;
    }


    private boolean brandHandler(String inputLine, JsonConverter jsonConverter, String jsonString, PrintWriter out, BufferedReader in) throws DaoException {
        try{
            DaoBrandImpl daoBrand = new DaoBrandImpl();
            JSONArray jsonArray;
            int intArgument = 0;
            if (inputLine.startsWith("getBrandById")) {
                intArgument = Integer.parseInt(inputLine.substring(inputLine.indexOf('.') + 1));
                inputLine = "getBrandById";
            } else if (inputLine.startsWith("deleteBrandById")) {
                intArgument = Integer.parseInt(inputLine.substring(inputLine.indexOf('.') + 1));
                inputLine = "deleteBrandById";
            }  else if (inputLine.startsWith("insertBrand")) {
                jsonString = inputLine.substring(inputLine.indexOf('.') + 1);
                inputLine = "insertBrand";
            } else if (inputLine.startsWith("getRelatedToBrandById")) {
                intArgument = Integer.parseInt(inputLine.substring(inputLine.indexOf('.') + 1));
                inputLine = "getRelatedToBrandById";
            }
            System.out.println(inputLine+"<<<<<<");
            switch (inputLine) {
                case "getAllBrand":
                    String getAllString = jsonConverter.brandsListJson(getAllBrands(daoBrand));
                    out.println(getAllString);
                    break;
                case "getBrandById":
                    String getByIdString = jsonConverter.brandToJson(getBrandById(daoBrand, intArgument));
                    out.println(getByIdString);
                    break;
                case "deleteBrandById":
                    int deletionResponse = daoBrand.delete(intArgument);
                    out.println(deletionResponse);
                    break;
                case "insertBrand":
                    jsonArray = new JSONArray(jsonString);
                    Brand toInsert = new Brand(jsonArray.getJSONObject(0));
                    String insertPhoneString = jsonConverter.brandToJson(daoBrand.insert(toInsert));
                    out.println(insertPhoneString);
                    break;

                case "getRelatedToBrandById":
                    if(daoBrand.existsById(intArgument)){
                        DaoMobilePhone daoMobilePhone = new DaoMobilePhoneImpl();
                        Brand brand = getBrandById(daoBrand, intArgument);
                        List<MobilePhone> phones = daoMobilePhone.getPhoneByBrand(intArgument);
                        JSONObject brandJson;
                        brandJson = jsonConverter.serializeBrand(brand);
                        String brandJsonString = brandJson.toString();
                        String phonesJsonString = jsonConverter.phonesListJson(phones);
                        out.println(brandJsonString + "/" + phonesJsonString);
                    } else {
                        out.println("Brand not found");
                    }


                    break;
                case "exitBrand":
                    System.out.println("Exiting...");
                    return true;
                default:
                    System.out.println("Invalid choice. Please try again.");

            }

        } catch (SQLException e) {
            System.out.println("Database issues " + e.getMessage());
        }
        return false;
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


    public List<Brand> getAllBrands(DaoBrand daoBrand) throws DaoException {
        // Feature 1
        List<Brand> list = daoBrand.getAll();

        return list;
    }

    public Brand getBrandById(DaoBrand daoBrand, int idToSearch) throws DaoException {
        // Feature 2
        Brand brand = daoBrand.getById(idToSearch);

        if (brand != null) {
            System.out.println("Id found:");
            System.out.println(brand);
        } else {
            System.out.println("No Phone found with ID: " + idToSearch);
        }
        return brand;
    }
}
