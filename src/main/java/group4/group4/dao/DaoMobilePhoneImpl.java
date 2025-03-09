package group4.group4.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.dto.MobilePhone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class DaoMobilePhoneImpl extends MySqlDao implements DaoMobilePhone {

    @Override
    public List<MobilePhone> getAll() throws DaoException {
        String sql = "select * from MobilePhone";
        List<MobilePhone> mobilePhones = new ArrayList<>();
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
        }catch (SQLException e){
            throw new DaoException("Error fetching expenses: " + e.getMessage());
        }
        return mobilePhones;
    }

    @Override
    public MobilePhone getById(int id) {
        return null;
    }

    @Override
    public MobilePhone insert(MobilePhone mobilePhone) {
        return null;
    }

    @Override
    public void update(int id, MobilePhone mobilePhone) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<MobilePhone> findByFilter(Comparator<MobilePhone> comparator) throws DaoException {
        List<MobilePhone> mobilePhones = getAll();
        List<MobilePhone> filteredMobilePhones = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        double price = 0.0;
        System.out.println("Please enter the threshold price: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Price has to be a number. Enter the threshold price: ");
            scanner.next();
        }
        price = scanner.nextDouble();
        scanner.nextLine();
        MobilePhone mobilePhone = new MobilePhone(price);
        for (MobilePhone phone : mobilePhones) {
            if(comparator.compare(phone, mobilePhone) >= 0) {
                filteredMobilePhones.add(phone);
            }
        }
        scanner.close();
        return filteredMobilePhones;
    }
}
