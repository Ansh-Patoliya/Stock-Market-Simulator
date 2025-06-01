package util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$"
    );

    private static final LocalDate MIN_DOB = LocalDate.of(1950, 1, 1);


    public static String validateRegistration(String username, String email, LocalDate dob, String password, String confirmPassword) {

        // Username checks
        if (username == null || username.trim().isEmpty()) {
            return "Username is required.";
        }
        if (!username.matches("^[A-Za-z ]+$")) {
            return "Username must contain only letters and spaces.";
        }

        // Email checks
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email format.";
        }

        // Date of Birth
        if (dob == null) {
            return "Date of Birth is required.";
        }
        LocalDate today = LocalDate.now(); // use the current system date

        if (dob.isBefore(MIN_DOB) || dob.isAfter(today)) {
            return "Date of Birth must be between Jan 1, 1950 and today.";
        }


        // Password checks
        if (password == null || password.isEmpty()) {
            return "Password is required.";
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.";
        }

        // Confirm password
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return "Please confirm your password.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        return null; // All validations passed
    }
}
