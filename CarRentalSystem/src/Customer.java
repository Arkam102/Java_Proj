package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public static Customer addCustomer(String name) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "INSERT INTO customers (customer_id, name) VALUES (?, ?)";
        String customerId = "CUS" + String.format("%04d", (int)(Math.random() * 10000));
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, customerId);
        statement.setString(2, name);
        statement.executeUpdate();
        return new Customer(customerId, name);
    }
    
    public static Customer getCustomerById(String customerId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, customerId);
        ResultSet resultSet = statement.executeQuery();
    
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            return new Customer(customerId, name);
        }
        return null;
    }    

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}
