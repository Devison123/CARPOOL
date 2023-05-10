import java.sql.*;
import java.io.*;
public class Booking {
    private int tripId;
    private String username;
    private int numSeats;
    public Booking(int tripId, String username, int numSeats ) {
        this.tripId = tripId;
        this.numSeats = numSeats;
        this.username = username;
    }
    public Booking(){

    };
    ///////////////////////////////////////////////////////////////////////////
    public void save(Connection connection) throws SQLException, IOException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Bookings (trip_id, username, num_seats) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setInt(1, this.tripId);
        statement.setString(2, this.username);
        statement.setInt(3, this.numSeats);
        statement.executeUpdate();
        // writeBookingDetailsToFile(connection, this.username,this.tripId);
        ResultSet resultSet = statement.getGeneratedKeys();

        resultSet.close();
        statement.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void displayByUsername(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "SELECT t.trip_id, u.username as Driver, t.car_model, t.start_location, t.end_location, DATE_FORMAT(t.start_time, '%Y-%m-%d %H:%i') as Start_Time, b.booking_id, b.num_seats, b.booking_status " +
            "FROM Trips t " +
            "JOIN Users u ON t.username = u.username " +
            "JOIN Bookings b ON t.trip_id = b.trip_id " +
            "WHERE b.username = ?"
        );
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
    
        // Print table header
        System.out.println("\u001B[34m+----------+-----------+-----------------+-----------------+---------------------+------------------+-----------+------------+----------------+\u001B[0m");
        System.out.println("\u001B[34m| Trip ID  | Driver    | Car Model       | Start Location  | End Location        | Start Time       | Booking ID| Num Seats  | Booking Status |\u001B[0m");
        System.out.println("\u001B[34m+----------+-----------+-----------------+-----------------+---------------------+------------------+-----------+------------+----------------+\u001B[0m");
        
        // Print table rows
        while (resultSet.next()) {
            int tripId = resultSet.getInt("trip_id");
            String driver = resultSet.getString("Driver");
            String carModel = resultSet.getString("car_model");
            String startLocation = resultSet.getString("start_location");
            String endLocation = resultSet.getString("end_location");
            String startTime = resultSet.getString("Start_Time");
            int bookingId = resultSet.getInt("booking_id");
            int numSeats = resultSet.getInt("num_seats");
            String bookingStatus = resultSet.getString("booking_status");
            System.out.printf("| %-8d | %-9s | %-15s | %-15s | %-19s | %-10s | %-9d | %-10d | %-14s |\n", tripId, driver, carModel, startLocation, endLocation, startTime, bookingId, numSeats, bookingStatus);
        }
    
        // Print table footer
        System.out.println("\u001B[34m+----------+-----------+-----------------+-----------------+---------------------+------------------+-----------+------------+----------------+\u001B[0m");
    
        resultSet.close();
        statement.close();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void cancelBooking(Connection connection, int booking_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Bookings SET booking_status = 'CANCELLED' WHERE booking_id = ?"
        );
        statement.setInt(1, booking_id);
        int rowsUpdated = statement.executeUpdate();
    
        if (rowsUpdated == 0) {
            System.out.printf("No booking found with ID %d\n", booking_id);
        } else {
            System.out.printf("Booking with ID %d has been cancelled\n", booking_id);
        }
    
        statement.close();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean doesBookingExist(Connection connection, int tripId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) as count FROM Bookings WHERE booking_id = ? "
        );
        statement.setInt(1, tripId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        resultSet.close();
        statement.close();
        return count > 0;
    }  
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getNumSeatsByTripId(Connection connection, int tripId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT num_seats FROM Bookings WHERE trip_id = ?"
        );
        statement.setInt(1, tripId);
        ResultSet resultSet = statement.executeQuery();
        
        int numSeats = 0;
        if (resultSet.next()) {
            numSeats = resultSet.getInt("num_seats");
        }
        
        resultSet.close();
        statement.close();
        
        return numSeats;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getNumTripIdByBookingid(Connection connection, int booking_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT trip_id FROM Bookings WHERE booking_id = ?"
        );
        statement.setInt(1, booking_id);
        ResultSet resultSet = statement.executeQuery();
        
        int tripId;
        if(resultSet.next()){
        tripId = resultSet.getInt("trip_id");
        }else{
            return 0;
        }
        resultSet.close();
        statement.close();
        
        return tripId;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void removeByUsername(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "DELETE FROM Bookings WHERE username = ?"
        );
        statement.setString(1, username);
        statement.executeUpdate();
        statement.close();
    }
   ////////////////////////////////////////////////////////////////
   public static boolean BookingExist(Connection connection,String username) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(
            "SELECT COUNT(*) as count FROM Bookings WHERE  username= ? AND booking_status !='CANCELLED'"
    );
    statement.setString(1, username);
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();
    int count = resultSet.getInt("count");
    resultSet.close();
    statement.close();
    return count > 0;
}   
/////////////////////////////////////////////////////////////////////////////////////////////////
public static String[] displayBookings(Connection connection) throws SQLException { 
    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Bookings");
    ResultSet resultSet = statement.executeQuery();
    if (!resultSet.next()) {
        return new String[] {"No bookings are available"};
    }

    StringBuilder output = new StringBuilder();
    output.append("┌────────────────┬───────────────────────────┬─────────────┬───────────────────────┬─────────────────────────┐\n");
    output.append(String.format("│ %-15s│ %-26s│ %-12s│ %-22s│ %-24s│\n", "Booking ID", "Trip ID", "Username", "Num_sets", "Booking status"));
    output.append("├────────────────┼───────────────────────────┼─────────────┼───────────────────────┼─────────────────────────┤\n");

    do {
        int bookingId = resultSet.getInt("booking_id");
        int tripId = resultSet.getInt("trip_id");
        String username = resultSet.getString("username");
        int num_seats = resultSet.getInt("num_seats");
        String booking_status = resultSet.getString("booking_status");
        output.append(String.format("│ %-15s│ %-26s│ %-12s│ %-22s│ %-24s│\n",bookingId, tripId, username, num_seats, booking_status ));
    } while (resultSet.next());
    output.append("└────────────────┴───────────────────────────┴─────────────┴───────────────────────┴─────────────────────────┘\n");

    return output.toString().split("\n");
}

// public void writeBookingDetailsToFile(Connection connection, String username) throws SQLException, IOException {
//     PreparedStatement statement = connection.prepareStatement(
//             "SELECT b.booking_id, b.trip_id, b.username, t.username AS driver_username, " +
//             "u.mobile_number AS driver_contact_number, CONCAT(u.firstname, ' ', u.lastname) AS driver_name, " +
//             "t.start_location, t.end_location, t.start_time, b.num_seats, t.car_model " +
//             "FROM Bookings b " +
//             "JOIN Trips t ON b.trip_id = t.trip_id " +
//             "JOIN Users u ON t.username = u.username " +
//             "WHERE b.username = ?"
//     );
//     statement.setString(1, username);
//     ResultSet resultSet = statement.executeQuery();

//     while (resultSet.next()) {
//         int bookingId = resultSet.getInt("booking_id");
//         int tripId = resultSet.getInt("trip_id");
//         String passengerUsername = resultSet.getString("username");
//         String driverUsername = resultSet.getString("driver_username");
//         String driverContactNumber = resultSet.getString("driver_contact_number");
//         String driverName = resultSet.getString("driver_name");
//         String startLocation = resultSet.getString("start_location");
//         String endLocation = resultSet.getString("end_location");
//         Timestamp startTime = resultSet.getTimestamp("start_time");
//         int numSeats = resultSet.getInt("num_seats");
//         String carModel = resultSet.getString("car_model");

//         // create file name using username and booking id
//         String fileName = username + "_" + bookingId + ".txt";
//         FileWriter writer = new FileWriter(fileName);
//         writer.write("Booking ID: " + bookingId + "\n");
//         writer.write("Trip ID: " + tripId + "\n");
//         writer.write("Passenger Username: " + passengerUsername + "\n");
//         writer.write("Driver Username: " + driverUsername + "\n");
//         writer.write("Driver Contact Number: " + driverContactNumber + "\n");
//         writer.write("Driver Name: " + driverName + "\n");
//         writer.write("Start Location: " + startLocation + "\n");
//         writer.write("End Location: " + endLocation + "\n");
//         writer.write("Start Time: " + startTime.toString() + "\n");
//         writer.write("Number of Seats Booked: " + numSeats + "\n");
//         writer.write("Car Model: " + carModel + "\n");
//         writer.close();
//     }

//     resultSet.close();
//     statement.close();
// }
//////////////////////////////////////////////////////
public static void writeBookingDetailsToFile(Connection connection, String username, int tripId) throws SQLException, IOException {
    PreparedStatement statement = connection.prepareStatement(
            "SELECT b.booking_id, b.trip_id, b.username, t.username AS driver_username, " +
            "u.mobile_number AS driver_contact_number, CONCAT(u.firstname, ' ', u.lastname) AS driver_name, " +
            "t.start_location, t.end_location, t.start_time, b.num_seats, t.car_model, b.booking_status " +
            "FROM Bookings b " +
            "JOIN Trips t ON b.trip_id = t.trip_id " +
            "JOIN Users u ON t.username = u.username " +
            "WHERE b.username = ? AND b.trip_id = ?"
    );
    statement.setString(1, username);
    statement.setInt(2, tripId);
    ResultSet resultSet = statement.executeQuery();

    while (resultSet.next()) {
        int bookingId = resultSet.getInt("booking_id");
        int tripIdResult = resultSet.getInt("trip_id");
        String passengerUsername = resultSet.getString("username");
        String driverUsername = resultSet.getString("driver_username");
        String driverContactNumber = resultSet.getString("driver_contact_number");
        String driverName = resultSet.getString("driver_name");
        String startLocation = resultSet.getString("start_location");
        String endLocation = resultSet.getString("end_location");
        Timestamp startTime = resultSet.getTimestamp("start_time");
        int numSeats = resultSet.getInt("num_seats");
        String carModel = resultSet.getString("car_model");
        String status = resultSet.getString("booking_status");

        // create file name using username and booking id
        String fileName = "Tickets/" + username + "_" + bookingId + ".txt";
        FileWriter writer = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(writer);

        // print header and booking details
        bw.write("--------------------------------------------------\n");
        bw.write("                     BOOKING TICKET                \n");
        bw.write("--------------------------------------------------\n");
        bw.write(String.format("%-20s: %d\n", "Booking ID", bookingId));
        bw.write(String.format("%-20s: %d\n", "Trip ID", tripIdResult));
        bw.write(String.format("%-20s: %s\n", "Passenger Username", passengerUsername));
        bw.write(String.format("%-20s: %s\n", "Driver Username", driverUsername));
        bw.write(String.format("%-20s: %s\n", "Driver Contact Number", driverContactNumber));
        bw.write(String.format("%-20s: %s\n", "Driver Name", driverName));
        bw.write(String.format("%-20s: %s\n", "Start Location", startLocation));
        bw.write(String.format("%-20s: %s\n", "End Location", endLocation));
        bw.write(String.format("%-20s: %s\n", "Start Time", startTime.toString()));
        bw.write(String.format("%-20s: %d\n", "Number of Seats Booked", numSeats));
        bw.write(String.format("%-20s: %s\n", "Car Model", carModel));
        bw.write(String.format("%-20s: %s\n", "Booking Status", status));

        // beautify the output
        bw.write("\n");
        bw.write("--------------------------------------------------\n");
        bw.write("                 THANK YOU FOR BOOKING             \n");
        bw.write("--------------------------------------------------\n");
        bw.write("\n");

        bw.close();
    }

    resultSet.close();
    statement.close();
}



    
}
