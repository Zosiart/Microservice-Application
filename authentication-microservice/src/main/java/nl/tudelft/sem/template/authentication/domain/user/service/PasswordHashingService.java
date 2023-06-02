package nl.tudelft.sem.template.authentication.domain.user.service;

import nl.tudelft.sem.template.authentication.domain.user.entity.HashedPassword;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A DDD service for hashing passwords.
 */
public class PasswordHashingService {

    private final transient PasswordEncoder encoder; // Uses SHA-512 encoding with salt

    public PasswordHashingService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public HashedPassword hash(String password) {
        return new HashedPassword(encoder.encode(password));
    }
}
