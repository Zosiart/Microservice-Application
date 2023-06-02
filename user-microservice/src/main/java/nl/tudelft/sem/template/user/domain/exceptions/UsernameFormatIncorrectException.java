package nl.tudelft.sem.template.user.domain.exceptions;

public class UsernameFormatIncorrectException extends Exception {
    static final long serialVersionUID = 1L;

    public UsernameFormatIncorrectException(String message) {
        super(message);
    }

    public UsernameFormatIncorrectException() {
        super("Username must be between 8 and 20 characters, only contain alphanumeric characters, underscores"
                + " and dots, and only start and end in alphanumeric characters.");
    }
}
