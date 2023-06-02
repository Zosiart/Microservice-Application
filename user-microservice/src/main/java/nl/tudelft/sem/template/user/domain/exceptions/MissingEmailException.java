package nl.tudelft.sem.template.user.domain.exceptions;

public class MissingEmailException extends Exception {
    static final long serialVersionUID = 1L;

    public MissingEmailException(String message) {
        super(message);
    }
}
