drop database carpoolApp;
CREATE DATABASE carpoolApp;

USE carpoolApp;

CREATE TABLE Users (
    username VARCHAR(50) PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL,
    gender VARCHAR(10) NOT NULL
);
CREATE TABLE Trips (
    trip_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    car_model VARCHAR(50) NOT NULL,
    start_location VARCHAR(255) NOT NULL,
    end_location VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    available_seats INT NOT NULL,
    luggage_space BOOLEAN NOT NULL,
    Trip_status VARCHAR(50) DEFAULT "TRIP CONFIRMED",
    FOREIGN KEY (username) REFERENCES Users(username)
);

CREATE TABLE Bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    trip_id INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    num_seats INT NOT NULL,
    booking_status VARCHAR(50) DEFAULT "BOOKING CONFIRMED",
    FOREIGN KEY (trip_id) REFERENCES Trips(trip_id),
    FOREIGN KEY (username) REFERENCES Users(username)
);
desc trips;
desc bookings;
insert into users values("daniel","1234","daniel@","1234567","m");
SELECT COUNT(*) FROM Users WHERE username = "daniel";
