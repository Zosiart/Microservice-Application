package nl.tudelft.sem.template.authentication.domain.user.exception;

public class UserNameAlreadyInUse extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public UserNameAlreadyInUse(String username) {
        super(username);
    }
}
