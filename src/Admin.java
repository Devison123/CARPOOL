import java.sql.*;

public class Admin {
    
    static boolean checkUsernameExists(Connection connection, String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Admin WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            return count > 0;
        }
    }
    public static boolean checkPassword(Connection connection, String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Admin WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return count > 0;
    }
}

