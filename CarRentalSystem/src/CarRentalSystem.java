package src;

import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarRentalSystem {

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
}
