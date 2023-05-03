import java.sql.*;

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
    public static void displayByUsername(Connection connection, String username) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(
        "SELECT * FROM Trips WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
    System.out.println("+--------+--------------+--------------+--------------------+--------------------+-------------------------+--------------------+");
    System.out.printf("|%-8s|%-14s|%-14s|%-20s|%-20s|%-25s|%-20s|\n", "Trip ID", "Username", "Car Model", "Start Location", "End Location", "Start Time", "Available_seats");
    System.out.println("+--------+--------------+--------------+--------------------+--------------------+-------------------------+--------------------+");

    while (resultSet.next()) {
        int tripId = resultSet.getInt("trip_id");
        String carModel = resultSet.getString("car_model");
        String startLocation = resultSet.getString("start_location");
        String endLocation = resultSet.getString("end_location");
        Timestamp startTime = resultSet.getTimestamp("start_time");
        int availableSeats = resultSet.getInt("available_seats");

        System.out.printf("|%-8d|%-14s|%-14s|%-20s|%-20s|%-25s|%-20d|\n", tripId, username, carModel, startLocation, endLocation, startTime.toString(), availableSeats);
    }

    System.out.println("+--------+--------------+--------------+--------------------+--------------------+-------------------------+--------------------+");
    resultSet.close();
    statement.close();
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void displayByLocations(Connection connection, String startLocation, String endLocation, int num_seats) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Trips WHERE start_location = ? AND end_location = ? AND available_seats <= ?");
        statement.setString(1, startLocation);
        statement.setString(2, endLocation);
        statement.setInt(3, num_seats);
        ResultSet resultSet = statement.executeQuery();
    
        System.out.println("+--------+--------------+--------------+--------------------+--------------------+-------------------------+--------------------+");
        System.out.printf("|%-8s|%-14s|%-14s|%-20s|%-20s|%-25s|%-20s|\n", "Trip ID", "Username", "Car Model", "Start Location", "End Location", "Start Time", "Available_seats");
        System.out.println("+--------+--------------+--------------+--------------------+--------------------+-------------------------+--------------------+");
    
        while (resultSet.next()) {
            int tripId = resultSet.getInt("trip_id");
            String username = resultSet.getString("username");
            String carModel = resultSet.getString("car_model");
            Timestamp startTime = resultSet.getTimestamp("start_time");
            int availableSeats = resultSet.getInt("available_seats");
    
            System.out.printf("|%-8d|%-14s|%-14s|%-20s|%-20s|%-25s|%-20d|\n", tripId, username, carModel, startLocation, endLocation, startTime.toString(), availableSeats);
        }
    
        System.out.println("+--------+--------------+--------------+--------------------+--------------------+-------------------------+--------------------+");
        resultSet.close();
        statement.close();
    }   
}
