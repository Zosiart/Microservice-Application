package nl.tudelft.sem.template.planning.exception;

public class ServiceNotAvailable extends Exception {
    static final long serialVersionUID = 1L;

    public ServiceNotAvailable(String message) {
        super(message);
    }
}
