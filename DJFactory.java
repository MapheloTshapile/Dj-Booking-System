package za.ac.cput.factory;
import za.ac.cput.domain.Booking;
import za.ac.cput.domain.DJ;
import za.ac.cput.domain.Review;
import za.ac.cput.utilities.Helper;
import za.ac.cput.domain.AvailabilityStatus; // Ensure this is imported for clarity

import java.util.List;

public class DJFactory {

    public static DJ createDj(Long djId, String stageName, String genre, double ratePerHour,
                              AvailabilityStatus availabilityStatus, String bio,
                              List<Booking> bookings, List<Review> reviews) {

        // 1. Validation for REQUIRED fields (stageName and genre)
        if (Helper.isNullOrEmpty(stageName)) {
            // This check (using Helper.isNullOrEmpty) handles null, "", AND " " (whitespace)
            throw new IllegalArgumentException("Stage name cannot be null, empty, or consist of only whitespace.");
        }
        if (Helper.isNullOrEmpty(genre)) {
            // This check (using Helper.isNullOrEmpty) handles null, "", AND " " (whitespace)
            throw new IllegalArgumentException("Genre cannot be null, empty, or consist of only whitespace.");
        }
        if (ratePerHour <= 0) {
            throw new IllegalArgumentException("Rate per hour must be a positive value.");
        }

        // 2. Handling for OPTIONAL fields
        // Trim the required strings to remove leading/trailing whitespace before building
        String finalStageName = stageName.trim();
        String finalGenre = genre.trim();

        // Handle optional fields and provide defaults if necessary
        String finalBio = (bio == null || bio.trim().isEmpty()) ? "" : bio.trim();

    // Images are stored as blobs now; no imageUrl field is required.

        // Handle default status
        AvailabilityStatus finalStatus = (availabilityStatus == null) ? AvailabilityStatus.AVAILABLE : availabilityStatus;

        return new DJ.Builder()
                .setDjId(djId)
                .setStageName(finalStageName)
                .setGenre(finalGenre)
                .setRatePerHour(ratePerHour)
                .setAvailabilityStatus(finalStatus)
                .setBio(finalBio)
                .setBookings(bookings)
                .setReviews(reviews)
                .build();
    }
}