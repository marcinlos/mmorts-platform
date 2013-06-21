package pl.edu.agh.ki.mmorts.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Cryptographic utilities.
 * 
 * @author los
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

    private static final char BASE64_PAD = '=';

    private static final String base64_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";

    /**
     * Chars used in base64-encoding
     */
    public static final char[] BASE64_CHARSET = base64_chars.toCharArray();

    /**
     * Base64-encoding of a binary data
     * 
     * @param b
     *            Binary data to be encoded
     * @return Encoded binary data
     */
    public static String toBase64(byte[] b) {
        StringBuilder sb = new StringBuilder();
        int diff = b.length % 3;
        int chars = 4 * ((b.length + 2) / 3) + diff;
        if (diff != 0) {
            chars -= 3;
        }
        for (int i = 0, j = 0; i < chars; ++i) {
            int r = i % 4;
            byte b_j = (j < b.length ? b[j] : 0);
            byte b_j1 = (j + 1 < b.length ? b[j + 1] : 0);
            if (r == 0) {
                sb.append(BASE64_CHARSET[63 & (b_j >>> 2)]);
            } else if (r == 1) {
                sb.append(BASE64_CHARSET[((b_j & 3) << 4) | (0xf & (b_j1 >> 4))]);
                ++j;
            } else if (r == 2) {
                sb.append(BASE64_CHARSET[((b_j & 0x0f) << 2)
                        | (3 & (b_j1 >>> 6))]);
                ++j;
            } else {
                sb.append(BASE64_CHARSET[b_j & 0x3f]);
                ++j;
            }
        }
        if (diff >= 1) {
            sb.append(BASE64_PAD);
        }
        if (diff < 2) {
            sb.append(BASE64_PAD);
        }
        return sb.toString();
    }

    /**
     * Decodes base65-encoded binary data
     * 
     * @param s
     *            String with base64-encoded data
     * @return Decoded data
     * @throws InvalidBase64Exception
     *             If the input string is not a valid base64-encoding
     */
    public static byte[] fromBase64(String s) throws Base64DecodeException {
        if (s.length() % 4 != 0) {
            throw new Base64DecodeException(s);
        }
        int pos = s.indexOf(BASE64_PAD);
        int padding = (pos == -1 ? 0 : s.length() - pos);
        int count = (s.length() / 4) * 3 - padding;
        byte[] b = new byte[count];

        for (int i = 0, j = 0; j < count; ++j) {
            int r = j % 3;
            byte b_i = (byte) base64_chars.indexOf(s.charAt(i));
            byte b_i1 = (byte) base64_chars.indexOf(s.charAt(i + 1));
            if (b_i == -1 || b_i1 == -1) {
                throw new Base64DecodeException(s);
            }
            if (r == 0) {
                b[j] = (byte) ((0xff & (b_i << 2)) | (b_i1 >>> 4));
                ++i;
            } else if (r == 1) {
                b[j] = (byte) ((0xff & (b_i << 4)) | (b_i1 >>> 2));
                ++i;
            } else {
                b[j] = (byte) ((0xff & (b_i << 6)) | b_i1);
                i += 2;
            }
        }
        return b;
    }

}
