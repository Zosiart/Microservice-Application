package nl.tudelft.sem.template.planning.authentication;

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
    public String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Interfaces with spring security to get the token of the user in the current context.
     *
     * @return The token of the user.
     */

    public String getToken() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    public Boolean checkUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
