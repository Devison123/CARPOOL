import java.sql.*;
public class Database {

    static Connection connect(){
        
        Connection connection = null;
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carpoolApp", "root","daniel123");
            System.out.println("Connected to database successfully!");
        }catch (ClassNotFoundException e) {
            System.out.println("Error: Could not find database driver");
            // e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: Could not connect to the database");
            // e.printStackTrace();
        }
    return connection;
    }
}
