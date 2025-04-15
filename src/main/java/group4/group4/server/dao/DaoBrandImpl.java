package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DaoBrandImpl extends MySqlDao implements DaoBrand {

    private DataSource ds;

    public DaoBrandImpl(DataSource ds) {
        this.ds = ds;
    }

    public DaoBrandImpl() {
    }

    @Override
    public List<Brand> getAll() throws DaoException {

        String sql = "select * from brand";
        List<Brand> brands = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = ds == null ? getConnection().prepareStatement(sql) : ds.getConnection().prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Brand brand = new Brand(id, name, description);
                brands.add(brand);
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching mobile phones: " + e.getMessage());
        }
        return brands;
    }


    @Override
    public Brand getById(int Id) throws DaoException {
        Brand brand = null;
        String query = "SELECT * FROM mobile_phone WHERE ID = ?";

        try (
                PreparedStatement preparedStatement = ds == null ? getConnection().prepareStatement(query) : ds.getConnection().prepareStatement(query);) {
            preparedStatement.setInt(1, Id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                brand = new Brand(id, name, description);
            }
        } catch (SQLException e) {
            throw new DaoException("getById() " + e.getMessage());
        }

        return brand;
    }

    @Override
    public Brand insert(Brand brand) throws SQLException {
        String query = "INSERT INTO brand (name, description) VALUES (?, ?)";
        try (
                Connection connection = (ds != null ? ds.getConnection() : getConnection());
        ) {


            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, brand.getName());
                preparedStatement.setString(2, brand.getDescription());
                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        brand.setId(generatedId);
                        System.out.println("generated id: " + generatedId);
                        System.out.println(brand);
                    } else {
                        throw new DaoException("Inserting  brand failed, no ID obtained.");
                    }
                }
            }

            return brand;
        } catch (SQLException e) {
            throw new DaoException("insert() failed " + e.getMessage());
        }
    }

    // Feature 5
    @Override
    public int update(int id, Brand brand) throws DaoException {
        if (id <= 0) throw new IllegalArgumentException("ID must be greater than zero");

        String query = "UPDATE brand SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement statement = ds == null? getConnection().prepareStatement(query): ds.getConnection().prepareStatement(query);
        ){

            statement.setString(1, brand.getName());
            statement.setString(2, brand.getDescription());
            statement.setInt(5, id);

            return statement.executeUpdate();
        }
        catch (SQLException e) { throw new DaoException("update() " + e.getMessage()); }
    }

    @Override
    public int delete(int id) throws DaoException {
        String deletePhonesQuery = "DELETE FROM mobile_phone WHERE id IN (SELECT id FROM mobile_phone WHERE brand_id = ?)";
        String deleteBrandQuery = "DELETE FROM brand WHERE id = ?";
        int affectedPhones = 0;
        try (
                Connection connection = (ds != null ? ds.getConnection() : getConnection());
        ){
            connection.setAutoCommit(false);
            try{
                try( PreparedStatement preparedStatement = connection.prepareStatement(deletePhonesQuery);
                ){
                    preparedStatement.setInt(1, id);
                    preparedStatement.executeUpdate();
                }

                try( PreparedStatement preparedStatement = connection.prepareStatement(deleteBrandQuery);){
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

    @Override
    public List<Brand> findByFilter(Comparator<Brand> comparator, double treshold) throws DaoException {
        return List.of();
    }

    @Override
    public boolean existsById(int id) throws DaoException {
        String sql = "SELECT COUNT(*) FROM brand WHERE id = ?";
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
            throw new DaoException("Error getting DB connection", e);
        }
        return false;
    }
}
