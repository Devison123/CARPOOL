class Trip {
    int tripId;
    String username;
    String carModel;
    String startLocation;
    String endLocation;
    LocalDateTime startTime;
    int availableSeats;
    boolean luggageSpace;

    // constructor
    Trip(String username, String carModel, String startLocation, String endLocation,
         LocalDateTime startTime, int availableSeats, boolean luggageSpace) {
        this.tripId = -1; // default value for a new trip
        this.username = username;
        this.carModel = carModel;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.availableSeats = availableSeats;
        this.luggageSpace = luggageSpace;
    }

    // create a new trip
    static void createTrip(String username, String carModel, String startLocation, String endLocation,
                           LocalDateTime startTime, int availableSeats, boolean luggageSpace) {
        // create a new trip object and insert it into the database
        Trip trip = new Trip(username, carModel, startLocation, endLocation, startTime, availableSeats, luggageSpace);
        Database.insertTrip(trip);

        System.out.println("Trip created successfully.");
    }

    // search for available trips
    static List<Trip> searchTrips(LocalDate dateOfTravel, String startLocation, String endLocation,
                                  LocalDateTime startTime, int requiredSeats, boolean luggageSpace) {
        // search the database for available trips based on the provided criteria
        List<Trip> trips = Database.searchTrips(dateOfTravel, startLocation, endLocation,
                                                       startTime, requiredSeats, luggageSpace);

        // filter the search results to show only trips within 5 hours of the searched trip
        List<Trip> filteredTrips = new ArrayList<>();
        for (Trip trip : trips) {
            if (Math.abs(Duration.between(trip.startTime, startTime).toHours()) <= 5) {
                filteredTrips.add(trip);
            }
        }

        return filteredTrips;
    }

    // book a trip
    void bookTrip(String username, int numSeats) {
        // check if the trip has enough available seats for the booking
        if (this.availableSeats < numSeats) {
            System.out.println("Not enough available seats.");
            return;
        }

        // create a new booking object and insert it into the database
        Booking booking = new Booking(this.tripId, username, numSeats);
        Database.insertBooking(booking);

        // update the available seats for the trip in the database
        this.availableSeats -= numSeats;
        Database.updateTrip(this);

        System.out.println("Booking created successfully.");
    }

    // cancel a trip
    void cancelTrip() {
        // delete the trip from the database
        Database.deleteTrip(this);

        System.out.println("Trip cancelled successfully.");
    }
}

