package mc.toriset.gloriousAuth.utils;

import org.mindrot.jbcrypt.BCrypt;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SecurityUtils {

    public static String securePassword(PasswordAlgorithm algo, String password) {
        switch (algo) {
            case SHA512 -> {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-512");
                    byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : hashedBytes) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }
                    return hexString.toString();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-512 algorithm not found", e);
                }
            }
            case PBKDF2 -> {
                try {
                    int iterations = 65536;
                    int keyLength = 256;
                    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), "salt".getBytes(StandardCharsets.UTF_8), iterations, keyLength);
                    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                    byte[] hash = skf.generateSecret(spec).getEncoded();
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : hash) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }
                    return hexString.toString();
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new RuntimeException("PBKDF2 algorithm not found", e);
                }
            }
            case BCRYPT -> {
                return BCrypt.hashpw(password, BCrypt.gensalt(12));
            }
            case ARGON2 -> {
                Argon2 argon2 = Argon2Factory.create();
                return argon2.hash(2, 65536, 1, password);
            }
            default -> throw new UnsupportedOperationException("Algorithm not supported: " + algo);
        }
    }

    public static boolean comparePassword(PasswordAlgorithm algo, String rawPassword, String hashedPassword) {
        switch (algo) {
            case SHA512 -> {
                String newHash = securePassword(PasswordAlgorithm.SHA512, rawPassword);
                return hashedPassword.equals(newHash);
            }
            case PBKDF2 -> {
                try {
                    int iterations = 65536;
                    int keyLength = 256;
                    PBEKeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), "salt".getBytes(StandardCharsets.UTF_8), iterations, keyLength);
                    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                    byte[] hash = skf.generateSecret(spec).getEncoded();
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : hash) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }
                    return hashedPassword.contentEquals(hexString);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new RuntimeException("PBKDF2 algorithm not found", e);
                }
            }
            case BCRYPT -> {
                return BCrypt.checkpw(rawPassword, hashedPassword);
            }
            case ARGON2 -> {
                Argon2 argon2 = Argon2Factory.create();
                return argon2.verify(hashedPassword, rawPassword);
            }
            default -> throw new UnsupportedOperationException("Algorithm not supported: " + algo);
        }
    }
}
