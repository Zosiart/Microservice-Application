package nl.tudelft.sem.template.user.domain.exceptions;

public class EmailFormatIncorrectException extends Exception {
    static final long serialVersionUID = 1L;

    public EmailFormatIncorrectException(String message) {
        super(message);
    }
}
