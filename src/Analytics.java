import java.util.*;
import java.sql.*;

public class Analytics {
    private static Scanner scanner = new Scanner(System.in);

    public static void analysis() {
        Scanner scan = new Scanner(System.in);
        Analytics analytics = new Analytics();
        boolean quit = false;
        while (!quit) {
            System.out.println("Choose an option:");
            Transactions.clearAndPrintTable(Menu.analytics);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Cancelled Bookings percentage is "+analytics.getCancelledPercentage());
                    System.out.println();
                    System.out.println("Press Enter to continue : ");
                    scan.nextLine();
                    break;
                case 2:
                    System.out.println("Cancelled trip percentage is "+analytics.getCancelledTripPercentage());
                    System.out.println();
                    System.out.println("Press Enter to continue : ");
                    scan.nextLine();

                    break;
                case 3:
                    analytics.displayNumSeatsHistogram() ;
                    System.out.println("Press Enter to continue : ");
                    scan.nextLine();
                    break;
                case 4:
                    analytics.displayGenderHistogram();
                    System.out.println("Press Enter to continue : ");
                    scan.nextLine();
                    break;
                case 5:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    public double getCancelledPercentage() {
        try {
            Connection conn = Database.connect(); // assuming you have a method that returns the database connection
            String sql = "SELECT COUNT(*) AS total_bookings, SUM(CASE WHEN booking_status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_bookings FROM Bookings";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int totalBookings = rs.getInt("total_bookings");
                int cancelledBookings = rs.getInt("cancelled_bookings");
                if (totalBookings == 0) {
                    return 0;
                } else {
                    return (double) cancelledBookings / totalBookings * 100;
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            // handle exception
            return 0;
        }
    }


    public double getCancelledTripPercentage() {
        try {
            Connection conn = Database.connect(); // assuming you have a method that returns the database connection
            String sql = "SELECT COUNT(*) AS total_trips, SUM(CASE WHEN Trip_status = 'TRIP CANCELLED' THEN 1 ELSE 0 END) AS cancelled_trips FROM Trips";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int totalTrips = rs.getInt("total_trips");
                int cancelledTrips = rs.getInt("cancelled_trips");
                if (totalTrips == 0) {
                    return 0;
                } else {
                    return (double) cancelledTrips / totalTrips * 100;
                }
            } else {
                return 0;
            }
        } catch (SQLException e) {
            // handle exception
            return 0;
        }
    }

    public static String getVerticalBars(int value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value; i++) {
            sb.append("|");
        }
        return sb.toString();
    }


    public void displayNumSeatsHistogram() {
        try {
            Connection conn = Database.connect(); // assuming you have a method that returns the database connection
            String sql = "SELECT num_seats, COUNT(*) AS num_bookings FROM Bookings GROUP BY num_seats";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
    
            // initialize a list to hold the histogram data
            List<Integer> histogramData = new ArrayList<>();
    
            // iterate over the result set and add the number of bookings for each num_seats value to the histogram data list
            while (rs.next()) {
                int numSeats = rs.getInt("num_seats");
                int numBookings = rs.getInt("num_bookings");
                histogramData.add(numBookings);
                System.out.println(numSeats + ": " +  getVerticalBars(numBookings));
            }
    
            
        } catch (SQLException e) {
            // handle exception
        }
    }
    
    
    public void displayGenderHistogram() {
        try {
            Connection conn = Database.connect(); // assuming you have a method that returns the database connection
            String sql = "SELECT gender, COUNT(DISTINCT u.username) AS num_users FROM users u INNER JOIN Bookings b ON u.username = b.username GROUP BY gender";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
    
            // initialize a list to hold the histogram data
            List<Integer> histogramData = new ArrayList<>();
    
            // iterate over the result set and add the number of users for each gender to the histogram data list
            while (rs.next()) {
                String gender = rs.getString("gender");
                int numUsers = rs.getInt("num_users");
                histogramData.add(numUsers);
                System.out.println(gender + ": " +  getVerticalBars(numUsers));
            }
    
            
        } catch (SQLException e) {
            // handle exception
        }
    }
    
   
    

    
    
}
