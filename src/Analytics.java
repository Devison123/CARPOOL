import java.util.*;
import java.sql.*;

public class Analytics {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Analytics analytics = new Analytics();
        boolean quit = false;
        while (!quit) {
            System.out.println("Choose an option:");
            System.out.println("1. Percentage of cancelled bookings");
            System.out.println("2. Percentage of cancelled trips");
            System.out.println("3. Insights on num_seats booked");
            System.out.println("4. Insights on users based on gender ");
            System.out.println("5. Quit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Cancelled Bookings percentage is "+analytics.getCancelledPercentage());
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Cancelled trip percentage is "+analytics.getCancelledTripPercentage());
                    System.out.println();

                    break;
                case 3:
                     analytics.displayNumSeatsHistogram() ;
                    break;
                case 4:
                    analytics.displayGenderHistogram();
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
                System.out.println(numSeats + ": " + numBookings);
            }
    
            // display the histogram
            System.out.println("Histogram:");
            for (int i = 1; i <= 10; i++) {
                String bar = "";
                for (int j = 0; j < histogramData.size(); j++) {
                    if (histogramData.get(j) >= i) {
                        bar += "|";
                    } else {
                        bar += " ";
                    }
                }
                System.out.println(i + " |" + bar);
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
                System.out.println(gender + ": " + numUsers);
            }
    
            // display the histogram
            System.out.println("Histogram:");
            String[] genders = {"M", "F"};
            for (int i = 0; i < genders.length; i++) {
                String bar = "";
                for (int j = 0; j < histogramData.size(); j++) {
                    if (i == 0 && genders[j].equals("M")) {
                        bar += getHistogramBar(histogramData.get(j));
                    } else if (i == 1 && genders[j].equals("F")) {
                        bar += getHistogramBar(histogramData.get(j));
                    } else {
                        bar += " ";
                    }
                }
                System.out.println(genders[i] + " |" + bar);
            }
        } catch (SQLException e) {
            // handle exception
        }
    }
    
    private String getHistogramBar(int count) {
        String bar = "";
        for (int i = 0; i < count; i++) {
            bar += "|";
        }
        return bar;
    }
    
    

    
    
}
