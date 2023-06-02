package nl.tudelft.sem.template.messaging.domain.message.exception;

public class InvalidMessageType extends Exception {
    static final long serialVersionUID = 1L;

    public InvalidMessageType(String message) {
        super(message);
    }
}
