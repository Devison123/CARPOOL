import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class User {

    private String username;
    private String password;
    private String email;
    private String mobileNumber;
    private String gender;
    private String firstname;
    private String lastname;

    public User( String username){
        this.username=username;
    }


    public User(String username, String password,String firstname,String lastname, String email, String mobileNumber, String gender) {
        this.username = username;
        this.firstname=firstname;
        this.lastname=lastname;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
    }
    public String getUsername() {
        return username;
    }

/////////////////////////////////////////////////////////////////////////////
    public void save(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Users (username,firstname,lastname, password, email, mobile_number, gender) VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
        statement.setString(1, this.username);
        statement.setString(2, this.firstname);
        statement.setString(3, this.lastname);
        statement.setString(4, this.password);
        statement.setString(5, this.email);
        statement.setString(6, this.mobileNumber);
        statement.setString(7, this.gender);
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.close();
        statement.close();
    }
    /////////////////////////////////////////////////////////////////////////////////////
    public static boolean checkUsernameExists(Connection connection, String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE BINARY username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            return count > 0;
        }
    }
    /////////////////////////////////////////////////////////////////////////////////
    public static boolean checkPassword(Connection connection, String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE username = ? AND BINARY password = ?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return count > 0;
    }
    ////////////////////////////////////////////////////////////////////////////////
    public static String[] profile(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        
        String[] userDetails = null;
        if (resultSet.next()) {
            String password = resultSet.getString("password");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String mobileNumber = resultSet.getString("mobile_number");
            String gender = resultSet.getString("gender");
            userDetails = new String[] {username, password, firstname, lastname, email, mobileNumber, gender};
        }
        
        resultSet.close();
        statement.close();
        
        // construct table with borders
        String[] table = new String[] {
            "\u001B[36m┌─────────────┬─────────────────┐",
            "│        USER PROFILE           │",
            "├─────────────┼─────────────────┤",
            "│ USERNAME    │ " + padRight(userDetails[0], 16) + "│",
            "├─────────────┼─────────────────┤",
            "│ PASSWORD    │ " + padRight("******", 16) + "│",
            "├─────────────┼─────────────────┤",
            "│ FIRST NAME  │ " + padRight(userDetails[2], 16) + "│",
            "├─────────────┼─────────────────┤",
            "│ LAST NAME   │ " + padRight(userDetails[3], 16) + "│",
            "├─────────────┼─────────────────┤",
            "│ EMAIL       │ " + padRight(userDetails[4], 16) + "│",
            "├─────────────┼─────────────────┤",
            "│ MOBILE NO.  │ " + padRight(userDetails[5], 16) + "│",
            "├─────────────┼─────────────────┤",
            "│ GENDER      │ " + padRight(userDetails[6], 16) + "│",
            "└─────────────┴─────────────────┘\u001B[0m"
        };
        
        return table;
    }
    //////////////////////////////////////////////////////////////////////////////
    public void editUser(Connection connection,String username,String newfirstname,String newlastname, String newPassword, String newEmail, String newMobileNumber, String newGender) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Users SET firstname = ?,lastname=?, password = ?, email = ?, mobile_number = ?, gender = ? WHERE username = ?"
        );
        
        statement.setString(1, newfirstname);
        statement.setString(2, newlastname);
        statement.setString(3, newPassword);
        statement.setString(4, newEmail);
        statement.setString(5, newMobileNumber);
        statement.setString(6, newGender);
        statement.setString(7, this.username);
        statement.executeUpdate();
        statement.close();
        // update the object state with the new details
        this.firstname=newfirstname;
        this.lastname=newlastname;
        this.password = newPassword;
        this.email = newEmail;
        this.mobileNumber = newMobileNumber;
        this.gender = newGender;
    }
    ////////////////////////////////////////////////////////////////////////////////
    public static void delete(Connection connection, String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Users WHERE username = ?");
        statement.setString(1, username);
        statement.executeUpdate();
        statement.close();
    }
    ///////////////////////////////////////////////////////////////////////////////
    public static String[] users(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
        ResultSet resultSet = statement.executeQuery();
        List<String> table = new ArrayList<>();
        table.add("\u001B[36m┌─────────────┬─────────────────┬──────────────┬──────────────┬────────────────┬─────────────────┬────────────┐");
        table.add("│ USERNAME    │ PASSWORD        │ FIRST NAME   │ LAST NAME    │ EMAIL          │ MOBILE NO.      │ GENDER     │");
        table.add("├─────────────┼─────────────────┼──────────────┼──────────────┼────────────────┼─────────────────┼────────────┤");
        while (resultSet.next()) {
            String[] userDetails = new String[7];
            userDetails[0] = resultSet.getString("username");
            userDetails[1] = "******";
            userDetails[2] = resultSet.getString("firstname");
            userDetails[3] = resultSet.getString("lastname");
            userDetails[4] = resultSet.getString("email");
            userDetails[5] = resultSet.getString("mobile_number");
            userDetails[6] = resultSet.getString("gender");
                table.add("│ " + padRight(userDetails[0], 11) + " │ " + padRight(userDetails[1], 15) + " │ " + padRight(userDetails[2], 12) + " │ " + padRight(userDetails[3], 12) + " │ " + padRight(userDetails[4], 14) + " │ " + padRight(userDetails[5], 15) + " │ " + padRight(userDetails[6], 10) + " │");
        }
    
        resultSet.close();
        statement.close();
    
        // construct table with borders
        
        table.add("└─────────────┴─────────────────┴──────────────┴──────────────┴────────────────┴─────────────────┴────────────┘\u001B[0m");
    
        return table.toArray(new String[0]);
    }
    
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
    
    ///////////////////////////////////////////////////////////////////////////
}
