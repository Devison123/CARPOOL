import java.sql.*;
import java.time.LocalDate;
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
    public static void updateSeats(Connection connection, int tripId, int numSeats, boolean isAddition) throws SQLException {
        String operator = isAddition ? "+" : "-";
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Trips SET available_seats = available_seats " + operator + " ? WHERE trip_id = ?");
        statement.setInt(1, numSeats);
        statement.setInt(2, tripId);
        statement.executeUpdate();
        statement.close();
        
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void delete(Connection connection,  String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "DELETE FROM Trips WHERE username = ?");
        statement.setString(1, username);
        statement.executeUpdate();
        statement.close();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void cancelTrip(Connection connection, int tripId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Trips SET trip_status = 'TRIP CANCELLED' WHERE trip_id = ?");
        statement.setInt(1, tripId);
        statement.executeUpdate();
        statement.close();
    }
    //////////////////////////////////////////////////////////////////////
    public static String[] displayByLocations(Connection connection, String startLocation, String endLocation, int numSeats, String dateStr, String username) throws SQLException {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT t.start_time, t.available_seats, t.trip_id, t.username, u.mobile_number, u.gender, t.car_model " +
                            "FROM Trips t " +
                            "JOIN Users u ON t.username = u.username " +
                            "WHERE t.start_location = ? " +
                            "AND t.end_location = ? " +
                            "AND t.available_seats >= ? " +
                            "AND DATE(t.start_time) = ?" +
                            "AND t.Trip_status = 'TRIP CONFIRMED'"
            );
            statement.setString(1, startLocation);
            statement.setString(2, endLocation);
            statement.setInt(3, numSeats);
            statement.setDate(4, Date.valueOf(date));
    
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return new String[] {"No cabs are available for this route on this date."};
            }
    
            StringBuilder output = new StringBuilder();
            output.append("┌────────────────┬───────────────────────────┬─────────────┬───────────────────────┬─────────────────────┬─────────────────────────┐\n");
            output.append(String.format("│ %-15s│ %-26s│ %-12s│ %-22s│ %-20s│ %-24s│\n", "Trip ID", "Driver Name", "Gender", "Contact No", "Car Model", "Start Date and Time"));
            output.append("├────────────────┼───────────────────────────┼─────────────┼───────────────────────┼─────────────────────┼─────────────────────────┤\n");
    
            do {
                int tripId = resultSet.getInt("trip_id");
                String fullName = resultSet.getString("username");
                String gender = resultSet.getString("gender");
                String contactNo = resultSet.getString("mobile_number");
                String carModel = resultSet.getString("car_model");
                Timestamp startTime = resultSet.getTimestamp("start_time");
    
                String startDateTimeStr = startTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy─MM─dd HH:mm"));
                output.append(String.format("│ %-15s│ %-26s│ %-12s│ %-22s│ %-20s│ %-24s│\n", tripId, fullName, gender, contactNo, carModel, startDateTimeStr));
            } while (resultSet.next());
            output.append("└────────────────┴───────────────────────────┴─────────────┴───────────────────────┴─────────────────────┴─────────────────────────┘\n");
    
            return output.toString().split("\n");
    
        } catch (Exception e) {
            return new String[] {"Invalid date format. Please enter date in the format yyyy-MM-dd."};
        }
    }
    
    

    //////////////////////////////////////////////////////////////////////////////////////////////
    public static void displayByUsername(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Trips WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
    
        if (!resultSet.next()) {
            System.out.println("\u001B[31mNo trips found for this username.\u001B[0m");
            return;
        }
    
        System.out.println("\u001B[32m────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\u001B[0m");
        System.out.printf("\u001B[32m|%-8s|%-17s|%-17s|%-17s|%-21s|%-20s|%-14s|%-15s|\u001B[0m\n", "Trip ID", "Car Model", "Start Location", "End Location", "Start Time", "Available Seats", "Luggage Space", "Trip Status");
        System.out.println("\u001B[32m────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\u001B[0m");
    
        do {
            int tripId = resultSet.getInt("trip_id");
            String carModel = resultSet.getString("car_model");
            String startLocation = resultSet.getString("start_location");
            String endLocation = resultSet.getString("end_location");
            Timestamp startTime = resultSet.getTimestamp("start_time");
            int availableSeats = resultSet.getInt("available_seats");
            boolean luggageSpace = resultSet.getBoolean("luggage_space");
            String tripStatus = resultSet.getString("trip_status");
    
            System.out.printf("\u001B[36m|%-8d|%-17s|%-17s|%-17s|%-21s|%-20d|%-14s|%-15s|\u001B[0m\n", tripId, carModel, startLocation, endLocation, startTime.toString(), availableSeats, luggageSpace, tripStatus);
        } while (resultSet.next());
    
        System.out.println("\u001B[32m────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\u001B[0m");
        resultSet.close();
        statement.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String[] displayTrips(Connection connection) throws SQLException { 
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM trips");
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return new String[] {"No cabs are available for this route on this date."};
            }
    
            StringBuilder output = new StringBuilder();
            output.append("┌────────────┬───────────────────────┬─────────────┬───────────────────┬───────────────────┬──────────────────────┐\n");
            output.append(String.format("│ %-11s│ %-22s│ %-12s│ %-18s│ %-18s│ %-21s│\n", "Trip ID", "User Name", "Car Model", "start_location", "end_location", "start_time","Trip_status"));
            output.append("├────────────┼───────────────────────┼─────────────┼───────────────────┼───────────────────┼──────────────────────┤\n");
    
            do {
                int tripId = resultSet.getInt("trip_id");
                String username = resultSet.getString("username");
                String carmodel = resultSet.getString("car_model");
                String start_location = resultSet.getString("start_location");
                String end_location = resultSet.getString("end_location");
                String Trip_status = resultSet.getString("Trip_status");
                Timestamp startTime = resultSet.getTimestamp("start_time");
    
                String startDateTimeStr = startTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy─MM─dd HH:mm"));
                output.append(String.format("│ %-11s│ %-22s│ %-12s│ %-18s│ %-18s│ %-21s│\n", tripId, username, carmodel, start_location, end_location, startTime,Trip_status));
            } while (resultSet.next());
            output.append("└────────────┴───────────────────────┴─────────────┴───────────────────┴───────────────────┴──────────────────────┘\n");
    
            return output.toString().split("\n");
        }
    }
    



    
    
    
     

