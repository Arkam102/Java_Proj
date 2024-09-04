package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    // Constructor
    public Car(String carId, String brand, String model, double basePricePerDay, boolean isAvailable) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = isAvailable;
    }

    public static Car getCarById(String carId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM cars WHERE car_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, carId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String brand = resultSet.getString("brand");
            String model = resultSet.getString("model");
            double basePricePerDay = resultSet.getDouble("base_price_per_day");
            boolean isAvailable = resultSet.getBoolean("is_available");
            return new Car(carId, brand, model, basePricePerDay, isAvailable);
        }
        return null;
    }

    public void updateAvailability(boolean available) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "UPDATE cars SET is_available = ? WHERE car_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setBoolean(1, available);
        statement.setString(2, this.carId);
        statement.executeUpdate();
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
