// import java.sql.*;
// import java.util.*;
// public class Booking {
//     private int tripId;
//     private int numSeats;
//     private String username;
//     public Booking(int tripId, String username, int numSeats ) {
//         this.tripId = tripId;
//         this.numSeats = numSeats;
//         this.username = username;
//     }

//     public static Booking createBookingFromInput(Connection connection) throws SQLException {
//         // Take input from the user for tripId, riderId, and numSeats
//         Scanner scanner = new Scanner(System.in);
//         System.out.print("Enter the trip ID: ");
//         int tripId = scanner.nextInt();
//         System.out.print("Enter the number of seats: ");
//         int numSeats = scanner.nextInt();
//         scanner.close();

//         // Create a new Booking object with the input values
//         Booking booking = new Booking(tripId, User.username ,numSeats);

//         // Save the booking to the database and set the bookingId field
//         booking.save(connection);

//         return booking;
//     }

//     public void save(Connection connection) throws SQLException {
//         PreparedStatement statement = connection.prepareStatement(
//                 "INSERT INTO Bookings (trip_id, username, num_seats) VALUES (?, ?, ?)",
//                 Statement.RETURN_GENERATED_KEYS
//         );
//         statement.setInt(1, this.tripId);
//         statement.setString(2, this.username);
//         statement.setInt(3, this.numSeats);
//         statement.executeUpdate();
//         ResultSet resultSet = statement.getGeneratedKeys();

//         resultSet.close();
//         statement.close();
//     }
// }
