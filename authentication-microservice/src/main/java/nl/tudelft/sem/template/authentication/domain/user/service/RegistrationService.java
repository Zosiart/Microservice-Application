package nl.tudelft.sem.template.authentication.domain.user.service;

import nl.tudelft.sem.template.authentication.application.client.UserManager;
import nl.tudelft.sem.template.authentication.domain.user.entity.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.entity.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.exception.PasswordTooWeak;
import nl.tudelft.sem.template.authentication.domain.user.exception.ServiceNotAvailable;
import nl.tudelft.sem.template.authentication.domain.user.exception.UserNameAlreadyInUse;
import nl.tudelft.sem.template.authentication.domain.user.repository.UserRepository;
import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new user.
 */

@Service
@PropertySource("classpath:application-dev.properties")
public class RegistrationService {

    private final transient UserRepository userRepository;
    private final transient PasswordHashingService passwordHashingService;
    private final transient UserManager userManager;
    private final Environment environment;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository         the user repository
     * @param passwordHashingService the password encoder
     * @param userManager            the user microservice api
     * @param environment            the environment
     */
    public RegistrationService(UserRepository userRepository, PasswordHashingService passwordHashingService,
                               UserManager userManager,
                               Environment environment) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
        this.userManager = userManager;
        this.environment = environment;
    }


    /**
     * Register a new user.
     *
     * @param request the RegistrationRequestModel that gets forwarded to the user microservice
     * @throws UserNameAlreadyInUse if the username is already in use
     */

    public AppUser registerUser(RegistrationRequestModel request)
        throws UserNameAlreadyInUse, PasswordTooWeak, ServiceNotAvailable {
        if (!checkIfUsernameIsUnique(request.getUsername())) {
            throw new UserNameAlreadyInUse(request.getUsername());
        }

        if (!checkIfPasswordIsStrong(request.getPassword())) {
            throw new PasswordTooWeak(request.getPassword());
        }

        HashedPassword hashedPassword = passwordHashingService.hash(request.getPassword());
        AppUser user = new AppUser(request.getUsername(), hashedPassword);
        try {
            ResponseEntity<Long> response = userManager.registerUser(environment.getProperty("service.user"), request);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new Exception("User service failed to register user");
            }
            user.setId(response.getBody());
            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceNotAvailable("User microservice is not available");
        }
        return user;
    }

    private boolean checkIfUsernameIsUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    //Password should have at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character
    private boolean checkIfPasswordIsStrong(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")
            && password.matches(".*[0-9].*")
            && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
}
