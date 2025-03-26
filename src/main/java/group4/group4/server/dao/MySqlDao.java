package group4.group4.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDao {

    public Connection getConnection() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/mobile_store";
        String user = "root";
        String password = "";
        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e){
            System.out.println("Driver not found");
            e.printStackTrace();
        } catch (SQLException e){
            System.out.println("Connection failed" + e.getMessage());
            System.exit(1);
        }
        return connection;
    }


}
