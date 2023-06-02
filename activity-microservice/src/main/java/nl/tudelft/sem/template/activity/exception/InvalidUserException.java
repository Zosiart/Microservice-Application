package nl.tudelft.sem.template.activity.exception;

public class InvalidUserException extends Exception {
    static final long serialVersionUID = -3042686055658047285L;

    public InvalidUserException(String message) {
        super(message);
    }
}
