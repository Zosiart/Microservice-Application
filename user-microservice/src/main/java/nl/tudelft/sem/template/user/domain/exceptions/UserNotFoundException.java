package nl.tudelft.sem.template.user.domain.exceptions;

public class UserNotFoundException extends Exception {
    static final long serialVersionUID = 1L;

    public UserNotFoundException(Long userId) {
        super("Couldn't find user matching id " + userId);
    }
}
