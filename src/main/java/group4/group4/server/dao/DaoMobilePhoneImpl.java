package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class DaoMobilePhoneImpl extends MySqlDao implements DaoMobilePhone {

    private DataSource ds;

    public DaoMobilePhoneImpl(DataSource ds) {
        this.ds = ds;
    }

    public DaoMobilePhoneImpl() {
    }

    // Feature 1
    @Override
    public List<MobilePhone> getAll() throws DaoException {

        String sql = "select * from mobile_phone";
        List<MobilePhone> mobilePhones = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = ds == null? getConnection().prepareStatement(sql): ds.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int brand_id = resultSet.getInt("brand_id");
                    String model = resultSet.getString("model");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("quantity");
                    MobilePhone phone = new MobilePhone(id,brand_id,model,quantity,price);
                    mobilePhones.add(phone);
                }
        } catch (SQLException e){
            throw new DaoException("Error fetching expenses: " + e.getMessage());
        }
        return mobilePhones;
    }

    // Feature 2
    @Override
    public MobilePhone getById(int Id) throws DaoException {
        MobilePhone phone2 = null;
        String query = "SELECT * FROM mobile_phone WHERE ID = ?";

        try(
            PreparedStatement preparedStatement = ds == null? getConnection().prepareStatement(query): ds.getConnection().prepareStatement(query);) {
            preparedStatement.setInt(1, Id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int brand_id = resultSet.getInt("brand_id");
                String model = resultSet.getString("model");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                phone2 = new MobilePhone(id,brand_id,model,quantity,price);
            }
        } catch (SQLException e) {
            throw new DaoException("getById() " + e.getMessage());
        }

        return phone2;
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
        String query = "UPDATE mobile_phone SET brand_id = ?, model = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = ds == null? getConnection().prepareStatement(query): ds.getConnection().prepareStatement(query);
                ){

            statement.setInt(1, mobilePhone.getBrandId());
            statement.setString(2, mobilePhone.getModel());
            statement.setInt(3, mobilePhone.getQuantity());
            statement.setDouble(4, mobilePhone.getPrice());
            statement.setDouble(5, id);

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
                throw new RuntimeException(e);
            }
            connection.commit();
            return affectedPhones;

        } catch (SQLException e) {
            throw new DaoException("delete() " + e.getMessage());
        }

    }

    // Feature 6
    @Override
    public List<MobilePhone> findByFilter(Comparator<MobilePhone> comparator, double price) throws DaoException {
        List<MobilePhone> mobilePhones = getAll();
        List<MobilePhone> filteredMobilePhones = new ArrayList<>();

        MobilePhone mobilePhone = new MobilePhone(price);
        for (MobilePhone phone : mobilePhones) {
            if(comparator.compare(phone, mobilePhone) >= 0) {
                filteredMobilePhones.add(phone);
            }
        }
        return filteredMobilePhones;
    }
}
