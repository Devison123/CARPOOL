class Booking {

    int tripId;
    User rider;
    int numSeats;

    // Constructor to initialize the Booking object
    Booking( int tripId, User rider, int numSeats) {
        this.tripId = tripId;
        this.rider = rider;
        this.numSeats = numSeats;
    }

    // Method to create a new booking for the given trip and rider
    static void createBooking(Trip trip, User rider, int numSeats) {
        // Check if the number of seats required is greater than the available seats in the trip
        if (numSeats > trip.availableSeats) {
            throw new IllegalArgumentException("Not enough seats available for this trip.");
        }

        // Create a new booking object

        // Add the booking object to the Bookings table in the database
        // Update the available seats in the Trips table for the booked trip
        Database.addBooking(booking);
        Database.updateAvailableSeats(trip.tripId, trip.availableSeats - numSeats);
    }

    // Method to cancel an existing booking
    void cancelBooking() {
        // Remove the booking object from the Bookings table in the database
        // Update the available seats in the Trips table for the booked trip
        Database.removeBooking(this);
        Trip trip = Database.getTripById(this.tripId);
        Database.updateAvailableSeats(this.tripId, trip.availableSeats + this.numSeats);
    }
}
