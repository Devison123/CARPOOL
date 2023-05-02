import java.sql.*;
import java.util.Scanner;

public class Main {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
String url = "jdbc:mysql://localhost:3306/carpooling";
String user1 = "root";
String password = "password";    

try {
    Database dbManager = new Database(url, user1, password);
    System.out.println("Connected to database!");
    
    boolean loggedIn = false;
    String loggedInUsername = "";
    while (!loggedIn) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String inputPassword = scanner.nextLine();
        if (dbManager.checkLogin(username, inputPassword)) {
            System.out.println("Login successful!");
            loggedIn = true;
            loggedInUsername = username;
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
    
    while (true) {
        System.out.println("\n1. Create trip");
        System.out.println("2. Search for trips");
        System.out.println("3. View your trips");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline character
        
        switch (choice) {
            case 1:
                System.out.print("Enter car model: ");
                String carModel = scanner.nextLine();
                System.out.print("Enter start location: ");
                String startLocation = scanner.nextLine();
                System.out.print("Enter end location: ");
                String endLocation = scanner.nextLine();
                System.out.print("Enter start time (YYYY-MM-DD HH:MM:SS): ");
                String startTimeString = scanner.nextLine();
                Timestamp startTime = Timestamp.valueOf(startTimeString);
                System.out.print("Enter available seats: ");
                int availableSeats = scanner.nextInt();
                scanner.nextLine(); // consume newline character
                System.out.print("Enter luggage space available (true or false): ");
                boolean luggageSpace = scanner.nextBoolean();
                scanner.nextLine(); // consume newline character
                boolean createTripResult = dbManager.createTrip(loggedInUsername, carModel, startLocation, endLocation, startTime, availableSeats, luggageSpace);
                if (createTripResult) {
                    System.out.println("Trip created successfully!");
                } else {
                    System.out.println("Failed to create trip. Please try again.");
                }
                break;
            case 2:
                System.out.print("Enter start location: ");
                String searchStartLocation = scanner.nextLine();
                System.out.print("Enter end location: ");
                String searchEndLocation = scanner.nextLine();
                System.out.print("Enter start time (YYYY-MM-DD HH:MM:SS): ");
                String searchStartTimeString = scanner.nextLine();
                Timestamp searchStartTime = Timestamp.valueOf(searchStartTimeString);
                System.out.print("Enter required number of seats: ");
                int requiredSeats = scanner.nextInt();
                scanner.nextLine(); // consume newline character
                System.out.print("Enter required luggage space (true or false): ");
                boolean requiredLuggageSpace = scanner.nextBoolean();
                scanner.nextLine(); // consume newline character
                ResultSet searchResult = dbManager.searchTrips(searchStartLocation, searchEndLocation, searchStartTime, requiredSeats, requiredLuggageSpace);
                if (searchResult.next()) {
                    System.out.println("Matching trips:");
                    while (searchResult.next()) {
                        int tripId = searchResult.getInt("trip_id");
                        String tripUsername = searchResult.getString("username");
                        String tripCarModel = searchResult.getString("car_model");
                        String tripStartLocation = searchResult.getString("start_location");
                        String tripEndLocation = searchResult.getString("end_location");
                        Timestamp tripStartTime = searchResult.getTimestamp("start_time");
                        int tripSeatsAvailable = searchResult.getInt("seats_available");
                        boolean tripLuggageSpaceAvailable = searchResult.getBoolean("luggage_space_available");
                        double tripPricePerSeat = searchResult.getDouble("price_per_seat");
                        System.out.println("Trip ID: " + tripId);
                        System.out.println("Driver: " + tripUsername);
                        System.out.println("Car model: " + tripCarModel);
                        System.out.println("Start location: " + tripStartLocation);
                        System.out.println("End location: " + tripEndLocation);
                        System.out.println("Start time: " + tripStartTime);
                        System.out.println("Seats available: " + tripSeatsAvailable);
                        System.out.println("Luggage space available: " + tripLuggageSpaceAvailable);
                        System.out.println("Price per seat: " + tripPricePerSeat);
                        }
                        } else {
                        System.out.println("No matching trips found.");
                        }
                        break;
                        case 3:
                        ResultSet tripsResult = dbManager.getTrips(username);
                        if (tripsResult.next()) {
                            System.out.println("Your trips:");
                            while (tripsResult.next()) {
                                int tripId = tripsResult.getInt("trip_id");
                                String tripCarModel = tripsResult.getString("car_model");
                                String tripStartLocation = tripsResult.getString("start_location");
                                String tripEndLocation = tripsResult.getString("end_location");
                                Timestamp tripStartTime = tripsResult.getTimestamp("start_time");
                                int tripAvailableSeats = tripsResult.getInt("available_seats");
                                boolean tripLuggageSpace = tripsResult.getBoolean("luggage_space");
                                // Display trip information
                                System.out.println("Trip ID: " + tripId);
                                System.out.println("Car Model: " + tripCarModel);
                                System.out.println("Start Location: " + tripStartLocation);
                                System.out.println("End Location: " + tripEndLocation);
                                System.out.println("Start Time: " + tripStartTime);
                                System.out.println("Available Seats: " + tripAvailableSeats);
                                System.out.println("Luggage Space: " + tripLuggageSpace);
                            }
                        } else {
                            System.out.println("You have not created any trips.");
                        }
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }
                        
                        
}
