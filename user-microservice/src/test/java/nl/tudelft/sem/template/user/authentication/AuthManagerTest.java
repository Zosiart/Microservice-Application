package nl.tudelft.sem.template.user.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthManagerTest {
    private transient AuthManager authManager;

    @BeforeEach
    void setUp() {
        authManager = new AuthManager();
    }

    @Test
    void getUsername() {
        String expected = "user123";
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                expected,
                null, List.of() // no credentials and no authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Act
        String actual = authManager.getUsername();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkUserAuthenticated() {
        String expected = "user123";
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                expected,
                null, List.of() // no credentials and no authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        // Assert
        assertTrue(authManager.checkUserAuthenticated());
    }
}