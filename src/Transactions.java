import java.sql.*;
import java.util.Scanner;

public class Transactions {
    static Scanner scanner = new Scanner(System.in);
    static String username;

    static boolean login(Connection connection) throws SQLException {
        System.out.println("LOGIN");
        System.out.print("Enter username: ");
        username = scanner.nextLine();
        if (!User.checkUsernameExists(connection, username)) {
            System.out.print("Username does not exists");
            return false;
        } else {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (User.checkPassword(connection, username, password)) {
                System.out.println("Signed in successfully");
                // User.profile(connection, username).display();
                return true;
            } else {
                System.out.println("INCORRECT PASSWORD");
                return false;
            }
        }
    }

    static void register(Connection connection) throws SQLException {
        System.out.println("REGISTER");
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (!User.checkUsernameExists(connection, username)) {
                break;
            }
            System.out.println("Username already exists, try again");
        }
        System.out.print("Enter firstname: ");
        String firstname = scanner.nextLine();
        System.out.print("Enter lastname: ");
        String lastname = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter mobile number: ");
        String mobileNumber = scanner.nextLine();
        System.out.print("Enter gender: ");
        String gender = scanner.nextLine();
        User newUser = new User(username, password,firstname,lastname, email, mobileNumber, gender);
        newUser.save(connection);
        System.out.println("Registered successfully");
    }

    static void addtrip(Connection connection) throws SQLException {
        System.out.print("Enter car model: ");
        String carModel = scanner.nextLine();
        System.out.print("Enter start location: ");
        String startLocation = scanner.nextLine();
        System.out.print("Enter end location: ");
        String endLocation = scanner.nextLine();
        System.out.print("Enter start time (YYYY-MM-DD HH:MM:SS): ");
        Timestamp startTime = Timestamp.valueOf(scanner.nextLine());
        System.out.print("Enter available seats: ");
        int availableSeats = scanner.nextInt();
        System.out.print("Enter luggage space (true or false): ");
        boolean luggageSpace = scanner.nextBoolean();
        Trip newTrip = new Trip(username, carModel, startLocation, endLocation, startTime, availableSeats,
                luggageSpace);
        newTrip.save(connection);
        System.out.println("New trip created");
    }

    static void createBooking(Connection connection) throws SQLException {
        // Take input from the user for tripId, riderId, and numSeats
        boolean trip=isTripListEmpty(connection);
        if(trip){
            System.out.println("No trip is Registered.try agian later");
            return ;
        }
        System.out.print("Enter start location: ");
        String startLocation = scanner.nextLine();
        System.out.print("Enter end location: ");
        String endLocation = scanner.nextLine();
        System.out.print("Enter the number of seats: ");
        int numSeats = scanner.nextInt();
        System.out.print("Enter start time (YYYY-MM-DD HH:MM:SS, e.g. 2023-05-05 10:00:00): ");
        try {
            Timestamp startTime = Timestamp.valueOf(scanner.nextLine());
            Trip booktrip=new Trip();
            booktrip.displayByLocations(connection, startLocation, endLocation, numSeats,startTime);
            // use startTime variable as needed
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid date/time format. Please enter a date/time in the format YYYY-MM-DD HH:MM:SS, e.g. 2023-05-05 10:00:00.");
        }

        // System.out.print("Enter  date (YYYY-MM-DD): ");
        // Date startDate = Date.valueOf(scanner.nextLine());

        System.out.print("Enter the trip ID: ");
        int tripId = scanner.nextInt();
        scanner.close();

        // Create a new Booking object with the input values
        Booking booking = new Booking(tripId, username, numSeats);

        // Save the booking to the database and set the bookingId field
        booking.save(connection);
    }

    ////////////////////////////////////////////////////////////////////////
    static boolean isTripListEmpty(Connection connection) throws SQLException {
        boolean isEmpty = true;
        String sql = "SELECT * FROM Trips";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    // static boolean isTripListEmptyForDate(Connection conn, Date date) throws SQLException {
    //     boolean isEmpty = true;
    //     String sql = "SELECT * FROM Trips WHERE start_time >= ?";
    //     try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    //         stmt.setDate(1, date);
    //         try (ResultSet rs = stmt.executeQuery()) {
    //             if (rs.next()) {
    //                 isEmpty = false;
    //             }
    //         }
    //     }
    //     return isEmpty;
    //}
    
    
}
