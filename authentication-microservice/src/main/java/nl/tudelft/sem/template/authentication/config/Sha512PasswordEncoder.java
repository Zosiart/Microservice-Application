package nl.tudelft.sem.template.authentication.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Sha512PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return hashWithSha512(rawPassword.toString());
    }

    @Override
    public boolean matches(
            CharSequence rawPassword, String encodedPassword) {
        String hashedPassword = encode(rawPassword);
        return encodedPassword.equals(hashedPassword);
    }

    @SuppressWarnings("checkstyle")
    private String hashWithSha512(String input) {
        StringBuilder result = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("salt".getBytes());

            byte [] digested = md.digest(input.getBytes());
            for (byte b : digested) {
                result.append(Integer.toHexString(0xFF & b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Incorrect Hashing Algorithm");
        }
    }
}
