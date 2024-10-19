package student.information.management.system;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    /**
     * Generates a random salt encoded in Base64.
     *
     * @return A Base64-encoded salt string.
     */
    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16]; // 16 bytes = 128 bits
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password with the provided salt using SHA-256.
     *
     * @param password The plain text password.
     * @param salt     The salt to use in hashing.
     * @return The hashed password as a hexadecimal string.
     */

    public static String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String combined = password + salt;
            byte[] hashedBytes = md.digest(combined.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found.", e);
        }
    }

    /**
     * Verifies if the provided password matches the hashed password using the provided salt.
     *
     * @param password       The plain text password.
     * @param salt           The salt used during hashing.
     * @param hashedPassword The hashed password stored in the database.
     * @return true if the password matches the hash, false otherwise.
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) {
        // Hash the provided password with the stored salt
        String hashedInputPassword = hashPasswordWithSalt(password, salt);

        // Compare the hashed input password with the stored hashed password
        return hashedInputPassword.equals(hashedPassword);
    }
}

