package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarRentalSystem {
    public List<Car> getAvailableCars() throws SQLException {
        List<Car> availableCars = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM cars WHERE is_available = true";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
    
        while (resultSet.next()) {
            String carId = resultSet.getString("car_id");
            String brand = resultSet.getString("brand");
            String model = resultSet.getString("model");
            double basePricePerDay = resultSet.getDouble("base_price_per_day");
            boolean isAvailable = resultSet.getBoolean("is_available");
            availableCars.add(new Car(carId, brand, model, basePricePerDay, isAvailable));
        }
        return availableCars;
    }
    

    public Car getCarById(String carId) throws SQLException {
        return Car.getCarById(carId);
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
        } else {
            System.out.println("Car was not rented.");
        }
    }
    
}
