import java.sql.*;
import java.util.*;
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
}
