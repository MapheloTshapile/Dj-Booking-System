package za.ac.cput.factory;

import za.ac.cput.domain.User;
import za.ac.cput.domain.Role;
import za.ac.cput.utilities.Helper;

import java.util.List;

public class UserFactory {

    public static User createUser(Long userId, String fullName, String email, String password, String phone, Role role, List<za.ac.cput.domain.Booking> bookings) {
        String validatedName = Helper.validateName(fullName);
        String validatedEmail = Helper.validateEmail(email);
        String validatedPassword = Helper.validatePassword(password);
        String validatedPhone = Helper.validateContactNumber(phone);

        return new User.Builder()
                .setUserId(userId)
                .setFullName(validatedName)
                .setEmail(validatedEmail)
                .setPassword(validatedPassword)
                .setPhone(validatedPhone)
                .setRole(role == null ? Role.EVENT_ORGANIZER : role)
                .setBookings(bookings)
                .build();
    }
}
