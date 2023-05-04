import java.sql.*;
public class User {

    private String username;
    private String password;
    private String email;
    private String mobileNumber;
    private String gender;
    private String firstname;
    private String lastname;


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
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE username = ?")) {
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
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE username = ? AND password = ?");
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
    public static User profile(Connection connection, String username) throws SQLException {
        User user = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String password = resultSet.getString("password");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String mobileNumber = resultSet.getString("mobile_number");
            String gender = resultSet.getString("gender");
            user = new User(username,firstname,lastname, password, email, mobileNumber, gender);
        }
        resultSet.close();
        statement.close();

        return user;
    }
    //////////////////////////////////////////////////////////////////////////////
    public void editUser(Connection connection,String username,String newfirstname,String newlastname, String newPassword, String newEmail, String newMobileNumber, String newGender) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Users SET firstname = ?,lastname=?, password = ?, email = ?, mobile_number = ?, gender = ? WHERE username = ?"
        );
        
        statement.setString(2, newfirstname);
        statement.setString(3, newlastname);
        statement.setString(4, newPassword);
        statement.setString(3, newEmail);
        statement.setString(4, newMobileNumber);
        statement.setString(5, newGender);
        statement.setString(6, this.username);
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
    public void display() {
        System.out.println("Username: " + this.username);
        System.out.println("Email: " + this.email);
        System.out.println("Mobile Number: " + this.mobileNumber);
        System.out.println("Gender: " + this.gender);
    }
    
}
