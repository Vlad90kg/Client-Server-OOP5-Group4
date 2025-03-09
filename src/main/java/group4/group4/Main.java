package group4.group4;

import group4.group4.Exceptions.DaoException;
import group4.group4.dao.DaoMobilePhone;
import group4.group4.dao.DaoMobilePhoneImpl;
import group4.group4.dto.MobilePhone;

import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws DaoException {
        DaoMobilePhone daoMobilePhone = new DaoMobilePhoneImpl();

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
    }
}
