package nl.tudelft.sem.template.authentication.domain.user.exception;

public class ServiceNotAvailable extends Exception {
    static final long serialVersionUID = 1L;

    public ServiceNotAvailable(String message) {
        super(message);
    }
}
