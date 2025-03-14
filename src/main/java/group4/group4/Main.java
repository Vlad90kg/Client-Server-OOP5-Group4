package group4.group4;

import group4.group4.Exceptions.DaoException;
import group4.group4.dao.DaoMobilePhone;
import group4.group4.dao.DaoMobilePhoneImpl;
import group4.group4.dto.MobilePhone;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws DaoException {
        DaoMobilePhone daoMobilePhone = new DaoMobilePhoneImpl();

        Scanner scanner = new Scanner(System.in);

        // Feature 1
        List<MobilePhone> list = daoMobilePhone.getAll();
        for (MobilePhone mobilePhone : list) {
            System.out.println(mobilePhone);
        }

        // Feature 3
        System.out.println("Enter ID to delete: ");

        int id = scanner.nextInt();
        scanner.nextLine();
        if(id != -1) {
            daoMobilePhone.delete(id);
            System.out.println("Id deleted successfully!");

            list = daoMobilePhone.getAll();
            for (MobilePhone mobilePhone : list) {
                System.out.println(mobilePhone);
            }
        }

        // Feature 2
        System.out.print("Enter the ID to search: ");
        id = scanner.nextInt();

        MobilePhone phone = daoMobilePhone.getById(id);

        if (phone != null) {
            System.out.println("Id found:");
            System.out.println(phone);
        } else {
            System.out.println("No Phone found with ID: " + id);
        }

        // Feature 6
        Comparator<MobilePhone> comparator =  (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice());
        list = daoMobilePhone.getAll();
        List<MobilePhone> phonesOver500 = daoMobilePhone.findByFilter(comparator);
        for (MobilePhone mobilePhone : list) {
            System.out.println(mobilePhone);
        }
        System.out.println("Phones over 500: ");

        for (MobilePhone mobilePhone : phonesOver500) {
            System.out.println( mobilePhone);
        }

        // Feature 4
        String newModel;
        int newBrandId, newQuantity;
        double newPrice;

        System.out.print("Enter brand ID: ");
        newBrandId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter model: ");
        newModel = scanner.nextLine();
        System.out.print("Enter quantity: ");
        newQuantity = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter price: ");
        newPrice = Double.parseDouble(scanner.nextLine());

        System.out.println("Added phone: " + daoMobilePhone.insert(new MobilePhone(newBrandId, newModel, newQuantity, newPrice)));
    }
}
