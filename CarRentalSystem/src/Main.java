package src;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CarRentalSystem system = new CarRentalSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Car Rental System");
            System.out.println("1. View available cars");
            System.out.println("2. Rent a car");
            System.out.println("3. Return a car");
            System.out.println("4. View rental history");
            System.out.println("5. Exit");
            System.out.print("Enter Your Choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        List<Car> availableCars = system.getAvailableCars();
                        if (availableCars.isEmpty()) {
                            System.out.println("No cars available.");
                        } else {
                            System.out.println("Available cars:");
                            for (Car car : availableCars) {
                                System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                            }
                        }
                        break;

                    case 2:
                        System.out.println("Enter Car ID to rent:");
                        String carId = scanner.nextLine();
                        Car car = Car.getCarById(carId);
                        if (car != null && car.isAvailable()) {
                            System.out.println("Enter your name:");
                            String name = scanner.nextLine();
                            Customer customer = Customer.addCustomer(name);

                            System.out.println("Enter number of rental days:");
                            int days;
                            try {
                                days = Integer.parseInt(scanner.nextLine().trim());

                                // Calculate rental cost
                                double rentalCost = car.calculatePrice(days);
                                System.out.println("The total rental cost for " + days + " days is: $" + rentalCost);
                                System.out.println("Do you want to proceed with the rental? (y/n)");

                                String confirmation = scanner.nextLine().trim().toLowerCase();
                                if (confirmation.equals("y")) {
                                    system.rentCar(car, customer, days);
                                } else {
                                    System.out.println("Rental cancelled.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number of days. Please enter a valid integer.");
                            }
                        } else {
                            System.out.println("Car is not available for rent.");
                        }
                        break;

                    case 3:
                        System.out.println("Enter Car ID to return:");
                        carId = scanner.nextLine();
                        car = Car.getCarById(carId);
                        if (car != null && !car.isAvailable()) {
                            system.returnCar(car);
                        } else {
                            System.out.println("Car was not rented or is already available.");
                        }
                        break;

                    case 4:
                        System.out.println("Enter Customer Name to view rental history:");
                        String customerName = scanner.nextLine();
                        system.viewRentalHistoryByCustomerName(customerName);
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (SQLException e) {
                System.out.println("An error occurred while processing the request.");
                e.printStackTrace();
            }
        }
    }
}
