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

        Comparator<MobilePhone> comparator =  Comparator.comparing(MobilePhone::getPrice);
        List<MobilePhone> list = daoMobilePhone.getAll();
        list.sort(comparator);
        for (MobilePhone mobilePhone : list) {
            System.out.println(mobilePhone);
        }
    }
}
