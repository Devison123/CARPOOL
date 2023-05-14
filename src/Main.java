import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = Database.connect();
        Scanner scan = new Scanner(System.in);
        String username;
        int loggedin;
        int choice = 0;
        do {
            System.out.println("**** WELCOME TO CARPOOL ****");
            Transactions.clearAndPrintTable(Menu.mainMenu);
            boolean validInput = false;

            while (!validInput) {
                System.out.print("Your choice: ");
                String input = scan.nextLine();
                try {
                    choice = Integer.parseInt(input);
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                }
            }
            switch (choice) {
                case 1:
                    loggedin = Transactions.login(connection);
                    username = Transactions.username;
                    if (loggedin==0) {
                        System.out.println("\nTry again / Try registering");
                        break;
                       
                    }
                    else if(loggedin==1){
                        int option;
                    do {
                        Transactions.clearAndPrintTable(Menu.userMenu);
                        System.out.print("Your choice : ");
                        option = Integer.parseInt(scan.nextLine());
                        switch (option) {
                            case 1:
                                // code for booking a trip
                                System.out.println("You chose option 1: BOOK A TRIP");
                                Transactions.createBooking(connection);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;
                            case 2:
                                // code for adding a trip
                                System.out.println("You chose option 2: ADD TRIP");
                                Transactions.addtrip(connection);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;
                            case 3:
                                // code for cancelling a booking
                                System.out.println("You chose option 3: CANCEL BOOKING");
                                Transactions.cancelBooking(connection);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;
                            case 4:
                                // code for cancelling a trip
                                System.out.println("You chose option 4: CANCEL TRIP");
                                Transactions.cancelTrip(connection);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;
                            case 5:
                                // code for editing account details
                                System.out.println("You chose option 5: EDIT ACCOUNT DETAILS");

                                System.out.print("Enter new first name: ");
                                String newfirstname = scan.nextLine();

                                System.out.print("Enter new last name: ");
                                String newlastname = scan.nextLine();

                                System.out.print("Enter new password: ");
                                String newPassword = scan.nextLine();

                                System.out.print("Enter new email: ");
                                String newEmail = scan.nextLine();

                                System.out.print("Enter new mobile number: ");
                                String newMobileNumber = scan.nextLine();
                                System.out.print("Enter your gender 'M' or 'F' :+");
                                String newgender = scan.nextLine().toUpperCase();
                                while (!newgender.equals("M") && !newgender.equals("F")) {
                                    System.out.println("Invalid gender, please enter 'M' or 'F': ");
                                    System.out.print("Enter gender (M/F): ");
                                    newgender = scan.nextLine().toUpperCase();
                                }

                                User user = new User(username);
                                user.editUser(connection, username, newfirstname, newlastname, newPassword, newEmail,
                                        newMobileNumber, newgender);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;
                            case 6:
                                // view trip
                                System.out.println(username);
                                Trip.displayByUsername(connection, username);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;
                            case 7:
                                // view booking
                                Booking.displayByUsername(connection, username);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;
                            case 8:
                                // view my profile
                                System.out.println("view My profile");
                                Transactions.clearAndPrintTable(User.profile(connection, username));
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                break;

                            case 9:
                                // delete profile
                                Transactions.removeUser(connection, username);
                                System.out.println("Press Enter to continue : ");
                                scan.nextLine();
                                option = 10;
                                break;
                            case 10:
                                // exit the program
                                System.out.println("You chose option 10: EXIT");
                                break;

                            default:
                                System.out.println("Invalid choice. Please enter a number between 1 and 10.");
                                break;
                        }
                        System.out.println(); // add a blank line for readability
                    } while (option != 10);//
                    }
                    else if(loggedin==2){
                        int z;
                        do {
                            Transactions.clearAndPrintTable(Menu.adminMenu);
                            System.out.print("Your choice : ");
                            z = Integer.parseInt(scan.nextLine());
                            switch (z) {
                                case 1:
                                    System.out.println("You chose option 1: ANALYTICS");
                                    Analytics.analysis();
                                    System.out.println("Press Enter to continue : ");
                                    scan.nextLine();
                                    break;
                                case 2:
                                    // code for adding a trip
                                    System.out.println("You chose option 2: VIEW USERS TABLE");
                                    Transactions.clearAndPrintTable(User.users(connection));
                                    System.out.println("Press Enter to continue : ");
                                    scan.nextLine();
                                    break;
                                case 3:
                                    System.out.println("You chose option 3: VIEW TRIPS TABLE");
                                    Transactions.clearAndPrintTable(Trip.displayTrips(connection));;
                                    System.out.println("Press Enter to continue : ");
                                    scan.nextLine();
                                    break;
                                case 4:
                                    // code for cancelling a trip
                                    System.out.println("You chose option 4: VIEW BOOKINGS TABLE");
                                    Transactions.clearAndPrintTable(Booking.displayBookings(connection));;
                                    System.out.println("Press Enter to continue : ");
                                    scan.nextLine();
                                    break;
                                case 5:
                                    // exit the program
                                    System.out.println("You chose option 10: EXIT");
                                    break;
    
                                default:
                                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                                    break;
                            }
                            System.out.println(); // add a blank line for readability
                        } while (z != 5);
                    }
                    break;
                case 2:
                    Transactions.register(connection);
                    break;

                case 3:
                    System.out.println("Exiting.....");
            }
        } while (choice != 3);

    }
}
