package src;

import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarRentalSystem {

    // Method to get available cars
    public List<Car> getAvailableCars() throws SQLException {
        List<Car> availableCars = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM cars WHERE is_available = true";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String carId = resultSet.getString("car_id");
            Car car = Car.getCarById(carId);
            if (car != null && car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public void rentCar(Car car, Customer customer, int days) throws SQLException {
        if (car.isAvailable()) {
            car.updateAvailability(false);
            Rental.rentCar(car, customer, days);
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) throws SQLException {
        car.updateAvailability(true);
        Rental rental = Rental.getRentalByCarId(car.getCarId());
        if (rental != null) {
            System.out.println("Car returned successfully!");
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void viewRentalHistoryByCustomerName(String customerName) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        
        String customerQuery = "SELECT * FROM customers WHERE name = ?";
        PreparedStatement customerStmt = connection.prepareStatement(customerQuery);
        customerStmt.setString(1, customerName);
        ResultSet customerResultSet = customerStmt.executeQuery();
        
        if (customerResultSet.next()) {
            String customerId = customerResultSet.getString("customer_id");
            String name = customerResultSet.getString("name");
            
            System.out.println("Rental History for Customer: " + name);

            String rentalQuery = "SELECT rentals.car_id, rentals.days, cars.brand, cars.model, cars.base_price_per_day " +
                                 "FROM rentals JOIN cars ON rentals.car_id = cars.car_id " +
                                 "WHERE rentals.customer_id = ?";
            PreparedStatement rentalStmt = connection.prepareStatement(rentalQuery);
            rentalStmt.setString(1, customerId);
            ResultSet rentalResultSet = rentalStmt.executeQuery();
            
            double totalAmountPaid = 0.0;
            boolean hasRentals = false;
            while (rentalResultSet.next()) {
                hasRentals = true;
                String carId = rentalResultSet.getString("car_id");
                String brand = rentalResultSet.getString("brand");
                String model = rentalResultSet.getString("model");
                int days = rentalResultSet.getInt("days");
                double basePricePerDay = rentalResultSet.getDouble("base_price_per_day");
                
                double rentalCost = basePricePerDay * days;
                totalAmountPaid += rentalCost;
                
                System.out.println("Car ID: " + carId + " - " + brand + " " + model);
                System.out.println("Rental Days: " + days + " - Rental Cost: $" + rentalCost);
            }
            
            if (hasRentals) {
                System.out.println("Total Amount Paid by " + name + ": $" + totalAmountPaid);
            } else {
                System.out.println("No rental history found for this customer.");
            }
        } else {
            System.out.println("Customer not found.");
        }
    }
}
