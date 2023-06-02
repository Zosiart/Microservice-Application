package nl.tudelft.sem.template.activity.exception;

public class WrongDateException extends Exception {
    static final long serialVersionUID = -687991492884005033L;

    public WrongDateException(String message) {
        super(message);
    }
}
