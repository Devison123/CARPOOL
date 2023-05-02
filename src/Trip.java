import java.sql.*;

public class Trip {
    private int tripId;
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
        if (resultSet.next()) {
            this.tripId = resultSet.getInt(1);
        }
        resultSet.close();
        statement.close();
    }
    public static void displayByUsername(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Trips WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int tripId = resultSet.getInt("trip_id");
            String carModel = resultSet.getString("car_model");
            String startLocation = resultSet.getString("start_location");
            String endLocation = resultSet.getString("end_location");
            Timestamp startTime = resultSet.getTimestamp("start_time");
            int availableSeats = resultSet.getInt("available_seats");
            boolean luggageSpace = resultSet.getBoolean("luggage_space");
            System.out.println("Trip ID: " + tripId);
            System.out.println("Car Model: " + carModel);
            System.out.println("Start Location: " + startLocation);
            System.out.println("End Location: " + endLocation);
            System.out.println("Start Time: " + startTime);
            System.out.println("Available Seats: " + availableSeats);
            System.out.println("Luggage Space: " + luggageSpace);
            System.out.println();
        }
        resultSet.close();
        statement.close();
    }
    public static void displayByLocations(Connection connection, String startLocation, String endLocation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Trips WHERE start_location = ? AND end_location = ?");
        statement.setString(1, startLocation);
        statement.setString(2, endLocation);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int tripId = resultSet.getInt("trip_id");
            String username = resultSet.getString("username");
            String carModel = resultSet.getString("car_model");
            Timestamp startTime = resultSet.getTimestamp("start_time");
            int availableSeats = resultSet.getInt("available_seats");
            boolean luggageSpace = resultSet.getBoolean("luggage_space");
            System.out.println("Trip ID: " + tripId);
            System.out.println("Username: " + username);
            System.out.println("Car Model: " + carModel);
            System.out.println("Start Location: " + startLocation);
            System.out.println("End Location: " + endLocation);
            System.out.println("Start Time: " + startTime);
            System.out.println("Available Seats: " + availableSeats);
            System.out.println("Luggage Space: " + luggageSpace);
            System.out.println();
        }
        resultSet.close();
        statement.close();
    }
    
}
