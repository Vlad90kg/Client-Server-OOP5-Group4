package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DaoMobilePhoneImpl extends MySqlDao implements DaoMobilePhone {

    private DataSource ds;

    public DaoMobilePhoneImpl(DataSource ds) {
        this.ds = ds;
    }

    public DaoMobilePhoneImpl() {
    }

    @Override
    public List<MobilePhone> getAll() throws DaoException {
        String sql = "SELECT m.id, m.brand_id, m.model, m.quantity, m.price, " +
                "s.storage, s.chipset " +
                "FROM mobile_phone m " +
                "LEFT JOIN phone_specifications s ON m.id = s.phone_id";
        List<MobilePhone> mobilePhones = new ArrayList<>();

        try (Connection connection = (ds != null ? ds.getConnection() : getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int brand_id = rs.getInt("brand_id");
                String model = rs.getString("model");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                MobilePhone phone = new MobilePhone(id, brand_id, model, quantity, price);


                String storage = rs.getString("storage");
                String chipset = rs.getString("chipset");


                if (storage != null || chipset != null) {
                    Specifications spec = new Specifications();
                    spec.setPhone_id(id);
                    spec.setStorage(storage);
                    spec.setChipset(chipset);
                    phone.setSpecifications(spec);
                }

                mobilePhones.add(phone);
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching mobile phones: " + e.getMessage(), e);
        }

        return mobilePhones;
    }

    // Feature 2
    @Override
    public MobilePhone getById(int id) throws DaoException {
        // Use a LEFT JOIN to retrieve the phone and its specifications for a given id.
        String sql = "SELECT m.id, m.brand_id, m.model, m.quantity, m.price, " +
                "s.storage, s.chipset " +
                "FROM mobile_phone m " +
                "LEFT JOIN phone_specifications s ON m.id = s.phone_id " +
                "WHERE m.id = ?";
        MobilePhone phone = null;

        try (Connection connection = (ds != null ? ds.getConnection() : getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int phoneId = rs.getInt("id");
                    int brand_id = rs.getInt("brand_id");
                    String model = rs.getString("model");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    // Create the MobilePhone object.
                    phone = new MobilePhone(phoneId, brand_id, model, quantity, price);

                    // Retrieve the specifications.
                    String storage = rs.getString("storage");
                    String chipset = rs.getString("chipset");

                    if (storage != null || chipset != null) {
                        Specifications spec = new Specifications();
                        spec.setPhone_id(phoneId);
                        spec.setStorage(storage);
                        spec.setChipset(chipset);
                        phone.setSpecifications(spec);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error in getById(): " + e.getMessage(), e);
        }

        return phone;
    }


    // Feature 4
    @Override
    public MobilePhone insert(MobilePhone mobilePhone) throws SQLException {
        String query = "INSERT INTO mobile_phone (brand_id, model, quantity, price) VALUES (?, ?, ?, ?)";
        String specQuery = "INSERT INTO phone_specifications (phone_id, storage, chipset) VALUES (?,?,?)";
        Specifications spec = mobilePhone.getSpecifications();
        System.out.println(mobilePhone);
        System.out.println("SPec" + spec);
        try (
                Connection connection = (ds != null ? ds.getConnection() : getConnection());
                        ) {
            connection.setAutoCommit(false);
            try {
                try(PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setInt(1, mobilePhone.getBrandId());
                    preparedStatement.setString(2, mobilePhone.getModel());
                    preparedStatement.setInt(3, mobilePhone.getQuantity());
                    preparedStatement.setDouble(4, mobilePhone.getPrice());
                    preparedStatement.executeUpdate();
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            mobilePhone.setId(generatedId);
                            spec.setPhone_id(generatedId);
                            System.out.println("generated id: " + generatedId);
                            System.out.println(mobilePhone);
                        } else {
                            throw new DaoException("Inserting mobile phone failed, no ID obtained.");
                        }
                    }
                }
                try (PreparedStatement ps = connection.prepareStatement(specQuery)) {
                    ps.setInt(1, spec.getPhone_id());
                    ps.setString(2, spec.getStorage());
                    ps.setString(3, spec.getChipset());
                    ps.executeUpdate();
                }

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            connection.commit();

            return mobilePhone;
        }
        catch (SQLException e) {
                throw new DaoException("insert() " + e.getMessage()); }


    }


    // Feature 5
    @Override
    public int update(int id, MobilePhone mobilePhone) throws DaoException {
        if (id <= 0) throw new IllegalArgumentException("ID must be greater than zero");

        String query = "UPDATE mobile_phone SET brand_id = ?, model = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = ds == null? getConnection().prepareStatement(query): ds.getConnection().prepareStatement(query);
                ){

            statement.setInt(1, mobilePhone.getBrandId());
            statement.setString(2, mobilePhone.getModel());
            statement.setInt(3, mobilePhone.getQuantity());
            statement.setDouble(4, mobilePhone.getPrice());
            statement.setInt(5, id);

            return statement.executeUpdate();
        }
        catch (SQLException e) { throw new DaoException("update() " + e.getMessage()); }
    }

    // Feature 3
    @Override
    public int delete(int id)throws DaoException {
        String deletePhoneSpecificationsQuery = "DELETE FROM phone_specifications WHERE phone_id IN (SELECT id FROM mobile_phone WHERE id = ?)";
        String deleteMobilePhoneQuery = "DELETE FROM mobile_phone WHERE id = ?";
        int affectedPhones = 0;
        try (
                Connection connection = (ds != null ? ds.getConnection() : getConnection());
        ){
            connection.setAutoCommit(false);
            try{
                try( PreparedStatement preparedStatement = connection.prepareStatement(deletePhoneSpecificationsQuery);
                ){
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                }

                try( PreparedStatement preparedStatement = connection.prepareStatement(deleteMobilePhoneQuery);){
                    preparedStatement.setInt(1, id);
                    affectedPhones = preparedStatement.executeUpdate();
                }
            }catch (SQLException e){
                connection.rollback();
                throw new DaoException("delete() rollback happened" + e.getMessage());
            }
            connection.commit();
            return affectedPhones;

        } catch (SQLException e) {
            throw new DaoException("delete()  is failed" + e.getMessage());
        }

    }

    // Feature 6
    @Override
    public List<MobilePhone> findByFilter(Comparator<MobilePhone> comparator, double price) throws DaoException {
        try {
            List<MobilePhone> mobilePhones = getAll();
            List<MobilePhone> filteredMobilePhones = new ArrayList<>();

            MobilePhone mobilePhone = new MobilePhone(price);
            for (MobilePhone phone : mobilePhones) {
                if(comparator.compare(phone, mobilePhone) >= 0) {
                    filteredMobilePhones.add(phone);
                }
            }
            return filteredMobilePhones;
        } catch (SQLException e) {
            throw new DaoException("findByFilter() " + e.getMessage());
        }

    }

    @Override
    public boolean existsById(int id) throws DaoException {
        String sql = "SELECT COUNT(*) FROM mobile_phone WHERE id = ?";

        try (Connection connection = (ds != null ? ds.getConnection() : getConnection());
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Error in existsById(): " + e.getMessage(), e);
        }

        return false;
    }

    @Override
    public List<MobilePhone> getPhoneByBrand(int brand) throws DaoException {
        String sql = "Select * from mobile_phone where brand_id = ?";
        List<MobilePhone> mobilePhones = new ArrayList<>();
        MobilePhone phone = new MobilePhone();
        try (Connection connection = (ds != null ? ds.getConnection() : getConnection());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, brand);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int phoneId = rs.getInt("id");
                    int brand_id = rs.getInt("brand_id");
                    String model = rs.getString("model");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    // Create the MobilePhone object.
                    phone = new MobilePhone(phoneId, brand_id, model, quantity, price);

                    // Retrieve the specifications.
//                    String storage = rs.getString("storage");
//                    String chipset = rs.getString("chipset");
//
//                    if (storage != null || chipset != null) {
//                        Specifications spec = new Specifications();
//                        spec.setPhone_id(phoneId);
//                        spec.setStorage(storage);
//                        spec.setChipset(chipset);
//                        phone.setSpecifications(spec);
//                    }
                    mobilePhones.add(phone);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching mobile phones: " + e.getMessage(), e);
        }
        return mobilePhones;
    }
}
