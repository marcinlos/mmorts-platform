package pl.agh.edu.ki.mmorts.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Cryptography utilities
 */
public class Crypto {

    private Crypto() {
        // Non-instantiable
    }

    /**
     * Converts byte array to hexadecimal string representation.
     * 
     * @param data
     *            Raw data - byte array
     * @return String representation in hexadecimal
     */
    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Computes SHA-256 hash, hiding all the necessary details of java
     * cryptography API. Hash is computed for
     * 
     * <pre>
     * encoded_pwd || salt
     * </pre>
     * 
     * where {@code encoded_pwd} is a UTF-8 encoding of the password string.
     * 
     * @param password
     *            Password
     * @param salt
     *            Salt
     * @return SHA-256 hash
     */
    public static byte[] computeHash(String password, byte[] salt) {
        MessageDigest hasher;
        try {
            hasher = MessageDigest.getInstance("SHA-256");
            hasher.update(salt);
            hasher.update(StringUtil.encode(password));
            return hasher.digest();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No SHA-256 provider available");
            throw new RuntimeException(e);
        }
    }

    /**
     * Simple function for checking whether password/salt combination matches
     * the hash
     * 
     * @param password
     *            Password
     * @param salt
     *            Salt
     * @param hashedPassword
     *            Hash of the original password/salt pair
     * @return {@code true} if password/salt matches the hash, {@code false}
     *         otherwise
     */
    public static boolean authenticate(String password, byte[] salt,
            byte[] hashedPassword) {
        byte[] value = Crypto.computeHash(password, salt);
        return Arrays.equals(hashedPassword, value);
    }
}
