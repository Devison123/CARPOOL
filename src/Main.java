import java.sql.*;
import java.util.Scanner;
public class main {
    static Scanner scanner = new Scanner(System.in);
    static void login(Connection connection) throws SQLException{
        System.out.println("Welcome to carpooling app");
        System.out.println("LOGIN");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if(!User.checkUsernameExists(connection,username)){
            System.out.print("Username does not exists");
        }else{
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if(User.checkPassword(connection, username, password)){
            System.out.println("Signed in successfully");
            User.profile(connection, username).display();;
        }else{
            System.out.println("INCORRECT PASSWORD");
        }
        }
    }
    static void register(Connection connection) throws SQLException{
        String username;
        System.out.println("REGISTER");
        while(true){
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if(!User.checkUsernameExists(connection,username)){
                break;
            }
            System.out.println("Username already exists, try again");
        }
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter mobile number: ");
            String mobileNumber = scanner.nextLine();
            System.out.print("Enter gender: ");
            String gender = scanner.nextLine();
            User newUser = new User(username, password, email, mobileNumber, gender);
            newUser.save(connection);
            System.out.println("Registered successfully");
    }

    public static void main(String[] args) throws SQLException {
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
        login(connection);
    }
}

