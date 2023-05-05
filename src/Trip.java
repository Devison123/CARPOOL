import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Trip {
    private String carModel;
    private String username;
    private String startLocation;
    private String endLocation;
    private Timestamp startTime;
    private int availableSeats;
    private boolean luggageSpace;

    public Trip(String username, String carModel, String startLocation, String endLocation, Timestamp startTime, int availableSeats, boolean luggageSpace) {
        this.username = username;
        this.carModel = carModel;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.availableSeats = availableSeats;
        this.luggageSpace = luggageSpace;
    }

    public Trip(){

    };
    ///////////////////////////////////////////////////////////////////////////////////////////
    public void save(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Trips (username, car_model, start_location, end_location, start_time, available_seats, luggage_space) VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, this.username);
        statement.setString(2, this.carModel);
        statement.setString(3, this.startLocation);
        statement.setString(4, this.endLocation);
        statement.setTimestamp(5, this.startTime);
        statement.setInt(6, this.availableSeats);
        statement.setBoolean(7, this.luggageSpace);
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.close();
        statement.close();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void updateSeats(Connection connection, int tripId, int numSeats, boolean isAddition) throws SQLException {
        String operator = isAddition ? "+" : "-";
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Trips SET available_seats = available_seats " + operator + " ? WHERE trip_id = ?");
        statement.setInt(1, numSeats);
        statement.setInt(2, tripId);
        statement.executeUpdate();
        statement.close();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void delete(Connection connection, int tripId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Trips WHERE trip_id = ?");
        statement.setInt(1, tripId);
        statement.executeUpdate();
        statement.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void displayByLocations(Connection connection, String startLocation, String endLocation, int numSeats, Timestamp date) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT t.start_time, t.available_seats, t.trip_id, t.username, CONCAT(u.first_name, ' ', u.last_name) AS full_name, u.contact_no, u.gender, t.car_model, t.start_location, t.end_location FROM Trips t JOIN Users u ON t.username = u.username WHERE t.start_location = ? AND t.end_location = ? AND t.available_seats >= ? AND t.start_time >= ?");
            statement.setString(1, startLocation);
            statement.setString(2, endLocation);
            statement.setInt(3, numSeats);
            statement.setTimestamp(4, date);
            ResultSet resultSet = statement.executeQuery();
    
            if (!resultSet.next()) {
                System.out.println("\u001B[31mNo cabs are available for this route on this date and time.\u001B[0m");
                return;
            }
    
            System.out.println("\u001B[32m+---------------------+--------------------+--------+-----------------------+----------------+--------+--------------+--------------------+--------------------+-----------------+\u001B[0m");
            System.out.printf("\u001B[32m|%-21s|%-20s|%-8s|%-23s|%-16s|%-8s|%-14s|%-20s|%-20s|%-17s|\u001B[0m\n", "Start Time", "Available Seats", "Trip ID", "Driver Name", "Contact No", "Gender", "Car Model", "Start Location", "End Location", "Username");
            System.out.println("\u001B[32m+---------------------+--------------------+--------+-----------------------+----------------+--------+--------------+--------------------+--------------------+-----------------+\u001B[0m");
    
            do {
                Timestamp startTime = resultSet.getTimestamp("start_time");
                int availableSeats = resultSet.getInt("available_seats");
                int tripId = resultSet.getInt("trip_id");
                String fullName = resultSet.getString("full_name");
                String contactNo = resultSet.getString("contact_no");
                String gender = resultSet.getString("gender");
                String carModel = resultSet.getString("car_model");
                String startLoc = resultSet.getString("start_location");
                String endLoc = resultSet.getString("end_location");
                String username = resultSet.getString("username");
    
                System.out.printf("\u001B[36m|%-21s|%-20d|%-8d|%-23s|%-16s|%-8s|%-14s|%-20s|%-20s|%-17s|\u001B[0m\n", startTime.toString(), availableSeats, tripId, fullName, contactNo, gender, carModel, startLoc, endLoc, username);
            } while (resultSet.next());
    
            System.out.println("\u001B[32m+---------------------+--------------------+--------+-----------------------+----------------+--------+--------------+--------------------+--------------------+-----------------+\u001B[0m");
            resultSet.close();
            statement.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
     
}
