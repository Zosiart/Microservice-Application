package nl.tudelft.sem.template.planning.exception;

public class InsufficientPermissions extends Exception {
    static final long serialVersionUID = 1L;

    public InsufficientPermissions(String message) {
        super(message);
    }
}
