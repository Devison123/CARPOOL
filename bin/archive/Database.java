import java.sql.*;

public class Database {
    private Connection conn;
    
    public Database(String url, String user, String password) throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
    }
    
    // User-related methods
    public boolean checkLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    public boolean createUser(String username, String password, String email, String mobileNumber, String gender) throws SQLException {
        if (checkUsernameExists(username)) {
            return false; // username already exists, cannot create user
        }
        String sql = "INSERT INTO Users (username, password, email, mobile_number, gender) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, email);
        stmt.setString(4, mobileNumber);
        stmt.setString(5, gender);
        int numRowsAffected = stmt.executeUpdate();
        return numRowsAffected == 1;
    }
    
    public boolean checkUsernameExists(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    // Trip-related methods
    public boolean createTrip(String username, String carModel, String startLocation, String endLocation, Timestamp startTime, int availableSeats, boolean luggageSpace) throws SQLException {
        String sql = "INSERT INTO Trips (username, car_model, start_location, end_location, start_time, available_seats, luggage_space) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, carModel);
        stmt.setString(3, startLocation);
        stmt.setString(4, endLocation);
        stmt.setTimestamp(5, startTime);
        stmt.setInt(6, availableSeats);
        stmt.setBoolean(7, luggageSpace);
        int numRowsAffected = stmt.executeUpdate();
        return numRowsAffected == 1;
    }
    
    public ResultSet searchTrips(String startLocation, String endLocation, Timestamp startTime, int requiredSeats, boolean luggageSpace) throws SQLException {
        String sql = "SELECT trip_id, username, car_model, start_location, end_location, start_time, available_seats, luggage_space FROM Trips WHERE start_location = ? AND end_location = ? AND start_time BETWEEN ? AND ? AND available_seats >= ? AND luggage_space = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, startLocation);
        stmt.setString(2, endLocation);
        stmt.setTimestamp(3, new Timestamp(startTime.getTime() - (5 * 60 * 60 * 1000))); // search for trips within 5 hours of the requested start time
        stmt.setTimestamp(4, new Timestamp(startTime.getTime() + (5 * 60 * 60 * 1000)));
        stmt.setInt(5, requiredSeats);
        stmt.setBoolean(6, luggageSpace);
        return stmt.executeQuery();
    }
    
    public ResultSet getUserTrips(String username) throws SQLException {
        String sql = "SELECT trip_id, car_model, start_location, end_location, start_time, available_seats, luggage_space FROM Trips WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    // Booking-related methods
public boolean createBooking(String username, int numPassengers) throws SQLException {
    // check if trip exists and has available seats
    String checkTripSql = "SELECT available_seats FROM Trips WHERE trip_id = ?";
    PreparedStatement checkTripStmt = conn.prepareStatement(checkTripSql);
    ResultSet checkTripRs = checkTripStmt.executeQuery();
    if (!checkTripRs.next()) {
        return false; // trip does not exist
    }
    int availableSeats = checkTripRs.getInt("available_seats");
    if (numPassengers > availableSeats) {
        return false; // not enough available seats
    }
    
    // create booking
    String createBookingSql = "INSERT INTO Bookings (username, trip_id, num_passengers) VALUES (?, ?, ?)";
    PreparedStatement createBookingStmt = conn.prepareStatement(createBookingSql);
    createBookingStmt.setString(1, username);
    createBookingStmt.setInt(2, numPassengers);
    int numRowsAffected = createBookingStmt.executeUpdate();
    
    // update available seats for trip
    String updateTripSql = "UPDATE Trips SET available_seats = ?";
    PreparedStatement updateTripStmt = conn.prepareStatement(updateTripSql);
    updateTripStmt.setInt(1, availableSeats - numPassengers);
    updateTripStmt.executeUpdate();
    
    return numRowsAffected == 1;
}



public ResultSet getTripBookings(int tripId) throws SQLException {
    String sql = "SELECT b.booking_id, u.username AS passenger_username, b.num_passengers FROM Bookings b INNER JOIN Users u ON b.username = u.username WHERE b.trip_id = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, tripId);
    ResultSet rs = stmt.executeQuery();
    return rs;
}

public boolean cancelBooking(int bookingId) throws SQLException {
    // check if booking exists
    String checkBookingSql = "SELECT trip_id, num_passengers FROM Bookings WHERE booking_id = ?";
    PreparedStatement checkBookingStmt = conn.prepareStatement(checkBookingSql);
    checkBookingStmt.setInt(1, bookingId);
    ResultSet checkBookingRs = checkBookingStmt.executeQuery();
    if (!checkBookingRs.next()) {
        return false; // booking does not exist
    }
    int tripId = checkBookingRs.getInt("trip_id");
    int numPassengers = checkBookingRs.getInt("num_passengers");
    
    // delete booking
    String deleteBookingSql = "DELETE FROM Bookings WHERE booking_id = ?";
    PreparedStatement deleteBookingStmt = conn.prepareStatement(deleteBookingSql);
    deleteBookingStmt.setInt(1, bookingId);
    int numRowsAffected = deleteBookingStmt.executeUpdate();
    
    // update available seats for trip
    String updateTripSql = "UPDATE Trips SET available_seats = available_seats + ? WHERE trip_id = ?";
    PreparedStatement updateTripStmt = conn.prepareStatement(updateTripSql);
    updateTripStmt.setInt(1, numPassengers);
    updateTripStmt.setInt(2,tripId);
    int numRowsAffected = updateTripStmt.executeUpdate();
    if (numRowsAffected == 1) {
    // create new booking record
    String createBookingSql = "INSERT INTO Bookings (username, trip_id, num_passengers) VALUES (?, ?, ?)";
    PreparedStatement createBookingStmt = conn.prepareStatement(createBookingSql);
    createBookingStmt.setString(1, username);
    createBookingStmt.setInt(2, tripId);
    createBookingStmt.setInt(3, numPassengers);
    int numRowsInserted = createBookingStmt.executeUpdate();
    return numRowsInserted == 1;
    } else {
    return false; // could not update available seats for trip
    }
    }
    
    public ResultSet getUserBookings(String username) throws SQLException {
    String sql = "SELECT * FROM Bookings WHERE username = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setString(1, username);
    ResultSet rs = stmt.executeQuery();
    return rs;
    }
    


public void close() throws SQLException {
conn.close();
}
}
