import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private String getGender() {
        return gender;
    }
    private String getEmail() {
        return email;
    }
    private String getMobileNumber() {
        return mobileNumber;
    }
    private String getLastName() {
        return lastname;
    }
    private String getFirstname() {
        return firstname;
    }
    private String getPassword() {
        return "******";
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
    public static void profile(Connection connection, String username) throws SQLException {
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
            user = new User(username, firstname, lastname, password, email, mobileNumber, gender);
        }
        resultSet.close();
        statement.close();
    
        if (user != null) {
            String[][] table = {
                {" "," "},
                {"Username", user.getUsername()},
                {"Password", user.getPassword()},
                {"First Name", user.getFirstname()},
                {"Last Name", user.getLastName()},
                {"Email", user.getEmail()},
                {"Mobile Number", user.getMobileNumber()},
                {"Gender", user.getGender()}
            };
    
            int[] columnWidths = new int[table[0].length];
            for (int i = 0; i < columnWidths.length; i++) {
                int maxWidth = 0;
                for (int j = 0; j < table.length; j++) {
                    String value = table[j][i];
                    if (value != null && value.length() > maxWidth) {
                        maxWidth = value.length();
                    }
                }
                columnWidths[i] = maxWidth;
            }
    
            String border = "+" + Arrays.stream(columnWidths).mapToObj(width -> "-".repeat(width + 2)).collect(Collectors.joining("+", "+", "+")) + "%n";
            String format = "|" + Arrays.stream(columnWidths).mapToObj(width -> " %-" + width + "s |").collect(Collectors.joining()) + "%n";
    
            System.out.format(border);
            System.out.format(format, (Object[]) table[0]);
            System.out.format(border);
    
            for (int i = 1; i < table.length; i++) {
                String[] row = table[i];
                Object[] formattedRow = new Object[row.length];
                for (int j = 0; j < row.length; j++) {
                    String value = row[j];
                    formattedRow[j] = (value != null) ? value : "";
                }
                System.out.format(format, formattedRow);
            }
    
            System.out.format(border);
        }
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
    public void display() {
        System.out.println("Username: " + this.username);
        System.out.println("Username: " + this.firstname);
        System.out.println("Username: " + this.lastname);
        System.out.println("Email: " + this.email);
        System.out.println("Mobile Number: " + this.mobileNumber);
        System.out.println("Gender: " + this.gender);
    }
    
}
