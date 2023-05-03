import java.sql.*;
public class main {
    public static void main(String[] args) throws SQLException {
        Connection connection = Database.connect();
        // if(Transactions.login(connection)){
        //     // createBooking(connection);
        //     // addtrip(connection);
        //     Trip.displayByUsername(connection,"daniel");
        // }
        Trip.displayByUsername(connection,"daniel");
    }
}

