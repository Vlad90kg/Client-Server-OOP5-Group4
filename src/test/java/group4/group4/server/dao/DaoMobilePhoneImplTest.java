package group4.group4.server.dao;

import group4.group4.server.dto.MobilePhone;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class DaoMobilePhoneImplTest {

    @Mock
    DataSource ds;

    @Mock
    Connection c;

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

    @Test
    void feature2() throws SQLException {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl(ds);

        when(ds.getConnection()).thenReturn(c);
        when(c.prepareStatement("SELECT * FROM mobile_phone WHERE ID = ?")).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getInt("brand_id")).thenReturn(1);
        when(rs.getString("model")).thenReturn("phone");
        when(rs.getInt("quantity")).thenReturn(10);
        when(rs.getDouble("price")).thenReturn(100.0);
        MobilePhone testPhone = dmpi.getById(1);

        when(rs.next()).thenReturn(false);
        MobilePhone nullPhone = dmpi.getById(15);

        assertThat(testPhone).isEqualTo(new MobilePhone(1, 1, "phone", 10, 100.0));
        assertThat(nullPhone).isEqualTo(null);
    }

    @Test
    void feature3() throws SQLException {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl(ds);

        when(ds.getConnection()).thenReturn(c);
        when(c.prepareStatement("DELETE FROM phone_specifications WHERE phone_id IN (SELECT id FROM mobile_phone WHERE id = ?)")).thenReturn(ps);
        when(c.prepareStatement("DELETE FROM mobile_phone WHERE id = ?")).thenReturn(ps);

        when(ps.executeUpdate()).thenReturn(1);
        assertThat(dmpi.delete(2)).isEqualTo(1);

        when(ps.executeUpdate()).thenReturn(0);
        assertThat(dmpi.delete(16)).isEqualTo(0);
    }

    @Test
    void  feature4() throws SQLException {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl(ds);

        when(ds.getConnection()).thenReturn(c);
        when(c.prepareStatement("INSERT INTO mobile_phone (brand_id, model, quantity, price) VALUES (?, ?, ?, ?)")).thenReturn(ps);

        when(ps.executeUpdate()).thenReturn(1);
        MobilePhone testPhone = new MobilePhone(1, "iPhone 14 Pro", 15, 999.99);
        assertThat(dmpi.insert(testPhone)).isEqualTo(testPhone);
    }

    @Test
    void feature5() throws SQLException {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        DaoMobilePhoneImpl dmpi = new DaoMobilePhoneImpl(ds);

        when(ds.getConnection()).thenReturn(c);
        when(c.prepareStatement("UPDATE mobile_phone SET brand_id = ?, model = ?, quantity = ?, price = ? WHERE id = ?")).thenReturn(ps);

        when(ps.executeUpdate()).thenReturn(1);
        assertThat(dmpi.update(1, new MobilePhone(1, "iPhone 16 Pro Max", 50, 1989.99))).isEqualTo(1);

        when(ps.executeUpdate()).thenReturn(0);
        assertThat(dmpi.update(16, new MobilePhone(1, "iPhone 16 Pro Max", 50, 1989.99))).isEqualTo(0);
    }

    @Test
    void feature6() throws SQLException {
        ds = mock(DataSource.class);
        c = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        when(ds.getConnection()).thenReturn(c);
        DaoMobilePhoneImpl dmpi = spy(new DaoMobilePhoneImpl(ds));

            List<MobilePhone> fixedPhones = Arrays.asList(
                    new MobilePhone(1, 1, "phone1", 1, 100.0),
                    new MobilePhone(2, 2, "phone2", 2, 1000.0),
                    new MobilePhone(3, 3, "phone3", 3, 5000.0),
                    new MobilePhone(4, 4, "phone4", 4, 300.0),
                    new MobilePhone(5, 4, "phone5", 5, 505.0)
            );

            doReturn(fixedPhones).when(dmpi).getAll();

            Comparator<MobilePhone> comparator = Comparator.comparing(MobilePhone::getPrice);

            List<MobilePhone> filteredList = dmpi.findByFilter(comparator, 500);

            List<MobilePhone> expectedList = Arrays.asList(
                    new MobilePhone(3, 3, "phone3", 3, 5000.0),
                    new MobilePhone(5, 4, "phone5", 5, 505.0),
                    new MobilePhone(2, 2, "phone2", 2, 1000.0)
                    );
            assertThat(expectedList).containsExactlyInAnyOrderElementsOf(filteredList);
    }

}
