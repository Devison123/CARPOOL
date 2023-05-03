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
    public static void displayBookingsByUsername(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Bookings WHERE username = ?"
        );
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
    
        // Print table header
        System.out.println("+----------+---------+----------+");
        System.out.println("| Trip ID  | Username | Num Seats |");
        System.out.println("+----------+---------+----------+");
    
        // Print table rows
        while (resultSet.next()) {
            int tripId = resultSet.getInt("trip_id");
            int numSeats = resultSet.getInt("num_seats");
            System.out.printf("| %-8d | %-7s | %-8d |\n", tripId, username, numSeats);
        }
    
        // Print table footer
        System.out.println("+----------+---------+----------+");
    
        resultSet.close();
        statement.close();
    }
    
}
