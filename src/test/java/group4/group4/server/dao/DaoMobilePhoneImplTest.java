package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dto.MobilePhone;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DaoMobilePhoneImplTest {

    @Mock
    DataSource ds;

    @Mock
    Connection c;

    @Mock
    Statement st;

    @Mock
    PreparedStatement ps;

    @Mock
    ResultSet rs;

    @Test
    void getAllTest() throws SQLException {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl(ds);
        when(ds.getConnection()).thenReturn(c);
        when(c.prepareStatement("select * from mobile_phone")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true,true, true, true, true, false);
        when(rs.getInt("id")).thenReturn(1,2,3,4,5);
        when(rs.getInt("brand_id")).thenReturn(1,2,3,4,4);
        when(rs.getString("model")).thenReturn("phone1","phone2","phone3","phone4","phone5");
        when(rs.getInt("quantity")).thenReturn(1,2,3,4,5);
        when(rs.getDouble("price")).thenReturn(100.0, 1000.0, 5000.0, 300.0, 505.0);

        List<MobilePhone> list = dmpi.getAll();

        assertThat(list).containsExactlyInAnyOrder(
                new MobilePhone(1,1,"phone1",1, 100.0),
                new MobilePhone(2,2,"phone2",2, 1000.0),
                new MobilePhone(3,3,"phone3",3, 5000.0),
                new MobilePhone(4,4,"phone4",4, 300.0),
                new MobilePhone(5,4,"phone5",5, 505.0)
                );
    }

//    @Test
//    void feature2() throws DaoException  {
//        MobilePhone testPhone = new MobilePhone(1, 1, "iPhone 14 Pro", 15, 999.99);
//        assertNull(dmpi.getById(15));
//
//        MobilePhone test = dmpi.getById(1);
//
//        assertAll(
//                () -> assertEquals(testPhone.getId(), test.getId()),
//                () -> assertEquals(testPhone.getBrandId(), test.getBrandId()),
//                () -> assertEquals(testPhone.getModel(), test.getModel()),
//                () -> assertEquals(testPhone.getQuantity(), test.getQuantity()),
//                () -> assertEquals(testPhone.getPrice(), test.getPrice())
//        );
//    }
//
//    @Test
//    void feature3() throws DaoException {
//        assertEquals(1, dmpi.delete(2));
//        assertEquals(0, dmpi.delete(16));
//    }
//
//    @Test
//    void feature4() throws DaoException {
//        MobilePhone testPhone = new MobilePhone(1, 1, "iPhone 14 Pro", 15, 999.99);
//        assertEquals(testPhone, dmpi.insert(testPhone));
//    }
//
//    @Test
//    void feature5() throws DaoException {
//        MobilePhone testPhone = new MobilePhone(1, "iPhone 16 Pro Max", 50, 1989.99);
//        assertEquals(1, dmpi.update(3, testPhone));
//    }


        @Test
    void feature6() throws DaoException {


    }

}
