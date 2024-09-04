package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Rental {
    private Car car;
    private Customer customer;
    private int days;
    private String rentalId; 

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public static void rentCar(Car car, Customer customer, int days) throws SQLException {
        if (car.isAvailable()) {
            car.updateAvailability(false); 
            Connection connection = DatabaseConnection.getConnection();
            String query = "INSERT INTO rentals (car_id, customer_id, days) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, car.getCarId());
            statement.setString(2, customer.getCustomerId());
            statement.setInt(3, days);
            statement.executeUpdate();
            
            System.out.println("Car rented successfully!");
        } else {
            System.out.println("Car is not available for rent.");
        }
    }    

    public static Rental getRentalByCarId(String carId) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM rentals WHERE car_id = ?"; 
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, carId);
        ResultSet resultSet = statement.executeQuery();
    
        if (resultSet.next()) {
            String customerId = resultSet.getString("customer_id");
            int days = resultSet.getInt("days");
            Car car = Car.getCarById(carId);
            Customer customer = Customer.getCustomerById(customerId);
            return new Rental(car, customer, days);
        }
        return null;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }
}
