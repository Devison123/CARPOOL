import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    public static boolean displayByLocations(Connection connection, String startLocation, String endLocation, int numSeats, Timestamp date) throws SQLException {
        try {
            // Splitting the given timestamp into year, month, day, and time components
            LocalDate localDate = date.toLocalDateTime().toLocalDate();
            LocalTime localTime = date.toLocalDateTime().toLocalTime();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();
            String time = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT t.start_time, t.available_seats, t.trip_id, t.username, u.mobile_number, u.gender, t.car_model " +
                    "FROM Trips t " +
                    "JOIN Users u ON t.username = u.username " +
                    "WHERE t.start_location = ? " +
                    "AND t.end_location = ? " +
                    "AND t.available_seats >= ? " +
                    "AND t.start_time >= ? " +
                    "AND t.start_time <= DATE_ADD(?, INTERVAL 1 HOUR)"
                
            );
            statement.setString(1, startLocation);
            statement.setString(2, endLocation);
            statement.setInt(3, numSeats);
            statement.setString(4, year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " " + time);
            statement.setString(5, time);
    
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("\u001B[31mNo cabs are available for this route on this date and time.\u001B[0m");
                return false;
            }
    
            System.out.println("\u001B[32m+--------+-----------------------+--------+--------+--------------+---------------------+\u001B[0m");
            System.out.printf("\u001B[32m|%-8s|%-23s|%-8s|%-8s|%-14s|%-21s|\u001B[0m\n", "Trip ID", "Driver Name", "Gender", "Contact No", "Car Model", "Start Time");
            System.out.println("\u001B[32m+--------+-----------------------+--------+--------+--------------+---------------------+\u001B[0m");
    
            do {
                int tripId = resultSet.getInt("trip_id");
                String fullName = resultSet.getString("username");
                String gender = resultSet.getString("gender");
                String contactNo = resultSet.getString("mobile_number");
                String carModel = resultSet.getString("car_model");
                Timestamp startTime = resultSet.getTimestamp("start_time");
    
                System.out.printf("\u001B[36m|%-8d|%-23s|%-8s|%-8s|%-14s|%-21s|\u001B[0m\n", tripId, fullName, gender, contactNo, carModel, startTime.toString());
            } while (resultSet.next());
    
            System.out.println("\u001B[32m+--------+-----------------------+--------+--------+--------------+---------------------+\u001B[0m");
            resultSet.close();
            statement.close();
    
            return true;
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    




}

    
    
    
     

