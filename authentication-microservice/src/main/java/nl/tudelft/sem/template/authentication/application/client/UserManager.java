package nl.tudelft.sem.template.authentication.application.client;

import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import org.springframework.http.ResponseEntity;

public interface UserManager {
    ResponseEntity<Long> registerUser(String userServiceAddress, RegistrationRequestModel registerUserRequestModel);
}
