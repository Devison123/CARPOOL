import java.sql.*;
import java.util.Scanner;
public class main {
    public static void main(String[] args) throws SQLException {
        Connection connection = Database.connect();
        Scanner scan = new Scanner(System.in); 
        String username;
        boolean loggedin;
        int choice=0;
        do{
            System.out.println("**** WELCOME TO CARPOOL ****");
            System.out.println("+--------------------+");
            System.out.println("|       MENU         |");
            System.out.println("+--------------------+");
            System.out.println("| 1) LOGIN           |");
            System.out.println("| 2) SIGN UP         |");
            System.out.println("| 3) EXIT            |");
            System.out.println("+--------------------+");
            System.out.print("Your choice : ");
            choice = Integer.parseInt(scan.nextLine());
        }while(choice!=3);
        if(Transactions.login(connection)){
            username = Transactions.username;
            Transactions.createBooking(connection);
            // addtrip(connection);
            // Trip.displayByUsername(connection,"daniel");
            Booking.displayByUsername(connection, username );
        }
        // Trip.displayByUsername(connection,"daniel");
        
    }
}

