package group4.group4.client;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dao.DaoMobilePhoneImpl;
import group4.group4.server.dto.MobilePhone;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws DaoException {
        Main mainInstance = new Main();
        DaoMobilePhone daoMobilePhone = new DaoMobilePhoneImpl();

        boolean exit = false;

        while (!exit) {
            System.out.println("=== Mobile Phone Management System ===");
            System.out.println("1. Display All Phones");
            System.out.println("2. Search Phone by ID");
            System.out.println("3. Delete Phone by ID");
            System.out.println("4. Insert Phone");
            System.out.println("5. Update Phone");
            System.out.println("6. Filter Phones by Price");
            System.out.println("7. Exit");
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
//                    mainInstance.getAllPhones(daoMobilePhone);
                    break;
                case 2:
                    mainInstance.getPhoneById(daoMobilePhone);
                    break;
                case 3:
                    mainInstance.deletePhoneById(daoMobilePhone);
                    break;
                case 4:
                    MobilePhone insertedPhone = mainInstance.insert(daoMobilePhone);
                    System.out.println("Inserted Phone: " + insertedPhone);
                    break;
                case 5:
                    mainInstance.update(daoMobilePhone);
                    break;
                case 6:
                    List<MobilePhone> filteredPhones = mainInstance.getFilteredPhones(daoMobilePhone);
                    System.out.println("Filtered Phones by Price:");
                    for (MobilePhone phone : filteredPhones) {
                        System.out.println(phone);
                    }
                    break;
                case 7:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
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

    public MobilePhone getPhoneById(DaoMobilePhone daoMobilePhone) throws DaoException {
        // Feature 2
        System.out.print("Enter the ID to search: ");
        int id = scanner.nextInt();

        MobilePhone phone = daoMobilePhone.getById(id);

        if (phone != null) {
            System.out.println("Id found:");
            System.out.println(phone);
        } else {
            System.out.println("No Phone found with ID: " + id);
        }
        return phone;
    }

    public void deletePhoneById(DaoMobilePhone daoMobilePhone) throws DaoException {
        // Feature 3
        System.out.println("Enter ID to delete: ");
        boolean validId = false;
        int id = -1;

        while (!validId) {
            try {
                id = Integer.parseInt(scanner.nextLine());
                if (id >= 0) {
                    validId = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
            System.out.println("Enter valid ID to delete: ");
        }
        scanner.nextLine();
        int rowsAffected = daoMobilePhone.delete(id);
        if (rowsAffected > 0) {
            System.out.println("Phone with ID " + id + " deleted successfully!");
        }
        else {
            System.out.println("No phone found with ID " + id);
        }
    }

    public MobilePhone insert(DaoMobilePhone daoMobilePhone) throws DaoException {
        // Feature 4
        String newModel = "";
        int newBrandId = 0, newQuantity = 0;
        double newPrice = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter brand ID: ");
                newBrandId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter model: ");
                newModel = scanner.nextLine();
                System.out.print("Enter quantity: ");
                newQuantity = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter price: ");
                newPrice = Double.parseDouble(scanner.nextLine());
                if (newBrandId > 0 || !newModel.isEmpty() || newQuantity >= 0 || newPrice > 0) {
                    validInput = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
            if (!validInput) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        return daoMobilePhone.insert(new MobilePhone(newBrandId, newModel, newQuantity, newPrice));

    }

    public void update(DaoMobilePhone daoMobilePhone) throws DaoException {
        // Feature 5
        String updatedModel = "";
        int idToUpdate = 0, updatedBrandId = 0, updatedQuantity = 0;
        double updatedPrice = 0;

        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter ID of mobile phone you would like to change: ");
                idToUpdate = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter new brand ID: ");
                updatedBrandId = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter new model: ");
                updatedModel = scanner.nextLine();
                System.out.print("Enter new quantity: ");
                updatedQuantity = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter new price: ");
                updatedPrice = Double.parseDouble(scanner.nextLine());

                if (idToUpdate >= 0 || updatedBrandId > 0 || !updatedModel.isEmpty() || updatedQuantity >= 0 || updatedPrice > 0) {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
            if (!validInput) {
                System.out.println("Invalid input. Please try again.");
            }
        }

        int rowsAffected = daoMobilePhone.update(idToUpdate, new MobilePhone(updatedBrandId, updatedModel, updatedQuantity, updatedPrice));
        if (rowsAffected > 0) {
            System.out.println("Phone with ID " + idToUpdate + " updated successfully!");
        }
        else {
            System.out.println("No phone found with ID " + idToUpdate);
        }

    }


    public List<MobilePhone> getFilteredPhones(DaoMobilePhone daoMobilePhone) throws DaoException {
        // Feature 6
        Comparator<MobilePhone> comparator = (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice());
        return daoMobilePhone.findByFilter(comparator);
    }

}
