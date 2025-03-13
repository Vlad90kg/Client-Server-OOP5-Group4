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
        Comparator<MobilePhone> comparator =  (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice());
        List<MobilePhone> list = daoMobilePhone.getAll();
        List<MobilePhone> phonesOver500 = daoMobilePhone.findByFilter(comparator);
        for (MobilePhone mobilePhone : list) {
            System.out.println(mobilePhone);
        }
        System.out.println("Phones over 500: ");

        for (MobilePhone mobilePhone : phonesOver500) {
            System.out.println( mobilePhone);
            System.out.println(mobilePhone);
        }
        System.out.println("Enter Brand ID to delete: ");

        int brand_id = scanner.nextInt();
        scanner.nextLine();
        daoMobilePhone.delete(brand_id);
        System.out.println("Brand id deleted successfully!");




        System.out.print("Enter the Brand ID to search: ");
        int id = scanner.nextInt();

        MobilePhone phone = daoMobilePhone.getById(id);

        if (phone != null) {
            System.out.println("Brand id found:");
            System.out.println(phone);
        } else {
            System.out.println("No Phone found with Brand ID: " + id);
        }

    }
}
