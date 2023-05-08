import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Transactions {
    static Scanner scanner = new Scanner(System.in);
    static String username;

    /////////////////////////////////////////////////////////////////////////////////////
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
                return true;
            } else {
                System.out.println("INCORRECT PASSWORD");
                return false;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
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
        System.out.print("Enter gender (M/F): ");
        String gender = scanner.nextLine().toUpperCase();
        while (!gender.equals("M") && !gender.equals("F")) {
        System.out.println("Invalid gender, please enter 'M' or 'F'");
        System.out.print("Enter gender (M/F): ");
        gender = scanner.nextLine().toUpperCase();}
        User newUser = new User(username, password, firstname, lastname, email, mobileNumber, gender);
        newUser.save(connection);
        System.out.println("Registered successfully");
    }

    ////////////////////////////////////////////////////////////////////////////////////
    static void addtrip(Connection connection) throws SQLException {
        System.out.print("Enter car model: ");
        String carModel = scanner.nextLine();
        System.out.print("Enter start location: ");
        String startLocation = scanner.nextLine();
        System.out.print("Enter end location: ");
        String endLocation = scanner.nextLine();
        System.out.print("Enter start time (YYYY-MM-DD HH:MM:SS): ");
        Timestamp startTime;
        while (true) {
            try {
                startTime = Timestamp.valueOf(scanner.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input, please enter a valid Timestamp in the format YYYY-MM-DD HH:MM:SS");
            }
        }

        System.out.print("Enter available seats: ");
        int availableSeats;
        while (true) {
            try {
                availableSeats = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid integer");
            }
        }

        System.out.print("Enter luggage space (true or false): ");
        String luggageSpaceString = scanner.nextLine().toLowerCase();
        boolean luggageSpace = false;
        while (!luggageSpaceString.equals("true") && !luggageSpaceString.equals("false")) {
            System.out.println("Invalid input, please enter 'true' or 'false'");
            System.out.print("Enter luggage space (true or false): ");
            luggageSpaceString = scanner.nextLine().toLowerCase();
        }
        if (luggageSpaceString.equals("true")) {
            luggageSpace = true;
        }

        Trip newTrip = new Trip(username, carModel, startLocation, endLocation, startTime, availableSeats,
                luggageSpace);
        newTrip.save(connection);
        System.out.println("New trip created");
    }

    //////////////////////////////////////////////////////////////////////////////////////
    static void createBooking(Connection connection) throws SQLException {
        // Take input from the user for tripId, riderId, and numSeats
        boolean trip = isTripListEmpty(connection);
        if (trip) {
            System.out.println("No trip is Registered. Try again later");
            return;
        }
        System.out.print("Enter start location: ");
        String startLocation = scanner.nextLine();
        System.out.print("Enter end location: ");
        String endLocation = scanner.nextLine();
        System.out.print("Enter number of seats: ");
        int numSeats;
        while (true) {
            try {
                numSeats = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid integer");
            }
        }
        scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD, e.g. 2023-05-05): ");
        try {
            String input = scanner.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(input, formatter);
            String dateString = date.toString();

            System.out.println("Input date: " + dateString);      
            clearAndPrintTable(Trip.displayByLocations(connection, startLocation, endLocation, numSeats, dateString,
            username));
            if(Trip.displayByLocations(connection, startLocation, endLocation, numSeats, dateString,
            username).length!=1){ 
            System.out.println("Enter the trip id");
            int tripId = scanner.nextInt();
            Booking book = new Booking(tripId, username, numSeats);
            book.save(connection);
            Trip.updateSeats(connection, tripId, numSeats, false);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter a valid date in YYYY-MM-DD format.");
            return;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    
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

    /////////////////////////////////////////////////////////////////////////////////
    static void cancelTrip(Connection connection) throws SQLException {
        Trip.displayByUsername(connection, username);
        System.out.print("Enter the trip id you want to cancel");
        int tripId = Integer.parseInt(scanner.nextLine());
        Trip.cancelTrip(connection, tripId);
        if (Booking.doesBookingExist(connection, tripId)) {
            Booking.cancelBooking(connection, tripId);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    static void cancelBooking(Connection connection) throws SQLException {
        if(!Booking.BookingExist(connection, username)){
            System.out.print("There is no booking listed in this username");
            System.out.println("press enter to continue ");
            return;
        }
        Booking.displayByUsername(connection, username);
        System.out.println();
        System.out.println("Enter the trip id");
        int tripId;
        while (true) {
            try {
                tripId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid integer for the trip ID");
            }
        }

        Booking.cancelBooking(connection, tripId);
        if (Booking.doesBookingExist(connection, tripId)) {
            Booking.cancelBooking(connection, tripId);
            int numseat = Booking.getNumSeatsByTripId(connection, tripId);
            Trip.updateSeats(connection, tripId, numseat, true);
        }
    }

    /////////////////////////////////////////////////////////////////////
    static void removeUser(Connection connection, String username) throws SQLException {
        System.out.print("Are you sure you want to delete your account!\nPlese enter your password to confirm.");
        String password = scanner.nextLine();
        if (User.checkPassword(connection, username, password)) {
            Trip.delete(connection, username);
            Booking.removeByUsername(connection, username);
            User.delete(connection, username);
        } else {
            System.out.println("INCORRECT PASSWORD");
        }

    }
    /////////////////////////////////////////////////////////////
    static void clearAndPrintTable(String[] table) {
        // Reset the console screen
        System.out.print("\u001Bc");
        System.out.flush();
    
        // Calculate the width of the console screen
        int screenWidth = 150; // assuming console screen width is 100 characters
    
        // Calculate the maximum width of the table without the escape sequences
        int tableWidth = 0;
        for (String row : table) {
            int rowWidth = row.replaceAll("\u001B\\[\\d+m", "").length();
            if (rowWidth > tableWidth) {
                tableWidth = rowWidth;
            }
        }
    
        // Calculate the left padding
        int leftPadding = (screenWidth - tableWidth) / 2;
    
        // Print the left padding and the table
        for (String row : table) {
            int rowWidth = row.replaceAll("\u001B\\[\\d+m", "").length();
            int rowLeftPadding = (tableWidth - rowWidth) / 2;
            for (int i = 0; i < leftPadding + rowLeftPadding; i++) {
                System.out.print(" ");
            }
            System.out.println(row);
        }
    }
    
    
    
    
    

}
