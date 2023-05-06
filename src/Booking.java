import java.sql.*;
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
    public void save(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Bookings (trip_id, username, num_seats) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setInt(1, this.tripId);
        statement.setString(2, this.username);
        statement.setInt(3, this.numSeats);
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();

        resultSet.close();
        statement.close();
    }
    ////////////////////////////////////////////////////////////////////////////
    public static void displayByUsername(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Bookings WHERE username = ?"
        );
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
    
        // Print table header
        System.out.println("+----------+----------+-----------+----------------+");
        System.out.println("| Trip ID  | Username | Num Seats | booking_status |");
        System.out.println("+----------+----------+-----------+----------------+");
    
        // Print table rows
        while (resultSet.next()) {
            int tripId = resultSet.getInt("trip_id");
            int numSeats = resultSet.getInt("num_seats");
            String booking_status = resultSet.getString("booking_status");
            System.out.printf("| %-8d | %-8s | %-9d | %-14s |\n", tripId, username, numSeats,booking_status);
        }
    
        // Print table footer
        System.out.println("+----------+----------+-----------+----------------+");
    
        resultSet.close();
        statement.close();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void cancelBooking(Connection connection, int trip_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Bookings SET booking_status = 'CANCELLED' WHERE trip_id = ?"
        );
        statement.setInt(1, trip_id);
        int rowsUpdated = statement.executeUpdate();
    
        if (rowsUpdated == 0) {
            System.out.printf("No booking found with ID %d\n", trip_id);
        } else {
            System.out.printf("Booking with ID %d has been cancelled\n", trip_id);
        }
    
        statement.close();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean doesBookingExist(Connection connection, int tripId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) as count FROM Bookings WHERE trip_id = ?"
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
    
}
