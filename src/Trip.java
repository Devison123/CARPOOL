// import java.sql.*;
// import java.util.Scanner;

// public class Trip {
//     private int tripId;
//     private String carModel;
//     private String startLocation;
//     private String endLocation;
//     private Timestamp startTime;
//     private int availableSeats;
//     private boolean luggageSpace;

//     public Trip(String carModel, String startLocation, String endLocation, Timestamp startTime, int availableSeats, boolean luggageSpace) {
//         this.carModel = carModel;
//         this.startLocation = startLocation;
//         this.endLocation = endLocation;
//         this.startTime = startTime;
//         this.availableSeats = availableSeats;
//         this.luggageSpace = luggageSpace;
//     }

//     public int getTripId() {
//         return tripId;
//     }

//     public void save(Connection connection) throws SQLException {
//         PreparedStatement statement = connection.prepareStatement(
//                 "INSERT INTO Trips (car_model, start_location, end_location, start_time, available_seats, luggage_space) VALUES (?, ?, ?, ?, ?, ?)",
//                 Statement.RETURN_GENERATED_KEYS
//         );
//         statement.setString(1, this.carModel);
//         statement.setString(2, this.startLocation);
//         statement.setString(3, this.endLocation);
//         statement.setTimestamp(4, this.startTime);
//         statement.setInt(5, this.availableSeats);
//         statement.setBoolean(6, this.luggageSpace);
//         statement.executeUpdate();
//         ResultSet resultSet = statement.getGeneratedKeys();
//         if (resultSet.next()) {
//             this.tripId = resultSet.getInt(1);
//         }
//         resultSet.close();
//         statement.close();
//     }

//     public static void main(String[] args) {
//         // Replace with your database URL, username, and password
//         String url = "jdbc:mysql://localhost:3306/mydb";
//         String user = "myuser";
//         String password = "mypassword";

//         try (Connection connection = DriverManager.getConnection(url, user, password)) {
//             Scanner scanner = new Scanner(System.in);
//             System.out.print("Enter car model: ");
//             String carModel = scanner.nextLine();
//             System.out.print("Enter start location: ");
//             String startLocation = scanner.nextLine();
//             System.out.print("Enter end location: ");
//             String endLocation = scanner.nextLine();
//             System.out.print("Enter start time (YYYY-MM-DD HH:MM:SS): ");
//             Timestamp startTime = Timestamp.valueOf(scanner.nextLine());
//             System.out.print("Enter available seats: ");
//             int availableSeats = scanner.nextInt();
//             System.out.print("Enter luggage space (true or false): ");
//             boolean luggageSpace = scanner.nextBoolean();

//             Trip newTrip = new Trip(carModel, startLocation, endLocation, startTime, availableSeats, luggageSpace);
//             newTrip.save(connection);
//             System.out.println("New trip created with ID: " + newTrip.getTripId());
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }


