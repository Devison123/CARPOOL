class User {
    String username;
    String password;
    String email;
    String mobileNumber;
    String gender;

    // constructor
    User(String username, String password, String email, String mobileNumber, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
    }

    // create a new user
    static void createUser(String username, String password, String email, String mobileNumber, String gender) {
        // check if username already exists in the database
        if (Database.checkUsernameExists(username)) {
            System.out.println("Username already exists. Please choose another username.");
            return;
        }

        // create a new user object and insert it into the database
        User user = new User(username, password, email, mobileNumber, gender);
        Database.insertUser(user);

        System.out.println("User created successfully.");
    }

    // log in the user
    static User login(String username, String password) {
        // check if username and password match in the database
        if (Database.checkCredentials(username, password)) {
            // return the User object for the logged in user
            return Database.getUser(username);
        } else {
            System.out.println("Incorrect username or password.");
            return null;
        }
    }

    // update user information
    void updateUserInfo(String email, String mobileNumber, String gender) {
        // update the user's information in the database
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        Database.updateUser(this);

        System.out.println("User information updated successfully.");
    }
}
