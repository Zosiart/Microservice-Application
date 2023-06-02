package nl.tudelft.sem.template.authentication.domain.user.event;

/**
 * A DDD domain event that indicated a user was created.
 */
public class UserWasCreatedEvent {
    private final String username;

    public UserWasCreatedEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
