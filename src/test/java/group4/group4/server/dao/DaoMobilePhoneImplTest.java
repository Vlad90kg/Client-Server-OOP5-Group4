package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dto.MobilePhone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class DaoMobilePhoneImplTest {
    DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl();

    @Test
    void feature2() throws DaoException  {
        MobilePhone testPhone = new MobilePhone(1, 1, "iPhone 14 Pro", 15, 999.99);
        assertNull(dmpi.getById(15));

        MobilePhone test = dmpi.getById(1);

        assertAll(
                () -> assertEquals(testPhone.getId(), test.getId()),
                () -> assertEquals(testPhone.getBrandId(), test.getBrandId()),
                () -> assertEquals(testPhone.getModel(), test.getModel()),
                () -> assertEquals(testPhone.getQuantity(), test.getQuantity()),
                () -> assertEquals(testPhone.getPrice(), test.getPrice())
        );
    }

    @Test
    void feature3() throws DaoException {
        assertEquals(1, dmpi.delete(2));
        assertEquals(0, dmpi.delete(16));
    }

    @Test
    void feature4() throws DaoException {
        MobilePhone testPhone = new MobilePhone(1, 1, "iPhone 14 Pro", 15, 999.99);
        assertEquals(testPhone, dmpi.insert(testPhone));
    }

    @Test
    void feature5() throws DaoException {
        MobilePhone testPhone = new MobilePhone(1, "iPhone 16 Pro Max", 50, 1989.99);
        assertEquals(1, dmpi.update(3, testPhone));
    }
}
