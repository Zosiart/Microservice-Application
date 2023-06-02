package nl.tudelft.sem.template.user.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authentication Manager.
 */
@Component
public class AuthManager {
    /**
     * Interfaces with spring security to get the name of the user in the current context.
     *
     * @return The name of the user.
     */
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Interfaces with spring security to check whether the user is authenticated within current context.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public Boolean checkUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

}
