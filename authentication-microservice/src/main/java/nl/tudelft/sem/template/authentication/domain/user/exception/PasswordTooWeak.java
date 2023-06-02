package nl.tudelft.sem.template.authentication.domain.user.exception;

public class PasswordTooWeak extends Exception {
    static final long serialVersionUID = -3387516993123229948L;

    public PasswordTooWeak(String password) {
        super(password);
    }
}
