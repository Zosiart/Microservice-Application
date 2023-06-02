package nl.tudelft.sem.template.user.controller;


import java.util.List;
import nl.tudelft.sem.template.user.application.services.UserService;
import nl.tudelft.sem.template.user.authentication.AuthManager;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.exceptions.MissingEmailException;
import nl.tudelft.sem.template.user.domain.exceptions.UserNotFoundException;
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.user.domain.models.RegistrationRequestModel;
import nl.tudelft.sem.template.user.domain.models.UserDataResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String USER_MISSING_EMAIL = "USER_MISSING_EMAIL";
    private static final String EMAIL_FORMAT_INCORRECT = "EMAIL_FORMAT_INCORRECT";
    private static final String USERNAME_FORMAT_INCORRECT = "USERNAME_FORMAT_INCORRECT";
    private static final String UNAUTHORIZED_USER = "User is not authenticated.";
    private final transient UserService userService;
    private final transient AuthManager authManager;

    @Autowired
    public UserController(UserService userService, AuthManager authManager) {
        this.userService = userService;
        this.authManager = authManager;
    }

    /**
     * Registers a new user.
     * This method is mapped to the `POST /register` endpoint and is used to register a new user.
     *
     * @param requestModel the request model containing the user's information
     * @return a response entity with the generated user ID and a status of `OK`
     */
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegistrationRequestModel requestModel) {
        if (!User.validUsername(requestModel.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_FORMAT_INCORRECT);
        }

        try {
            Long generatedUserId = userService.createUser(requestModel);
            return new ResponseEntity<>(generatedUserId, HttpStatus.OK);
        } catch (UsernameFormatIncorrectException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_FORMAT_INCORRECT, e);
        }
    }

    /**
     * Retrieves messaging information for a user.
     * This method is mapped to the `GET /messagingInfo` endpoint and is used to retrieve messaging
     * information for a user.
     *
     * @param id the ID of the user
     * @return a response entity with the messaging information and a status of `OK`
     * @throws ResponseStatusException if the user is not authenticated or if the user is not found or
     *                                 if the user is missing an email
     */
    @GetMapping("/messagingInfo/{id}")
    public ResponseEntity<MessagingInfoResponseModel> retrieveMessagingInformation(@PathVariable Long id) {
        try {
            MessagingInfoResponseModel messagingInfoResponseModel = userService.retrieveUserMessagingInformation(id);
            return new ResponseEntity<>(messagingInfoResponseModel, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        } catch (MissingEmailException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_MISSING_EMAIL, e);
        }
    }

    /**
     * Updates a user's information.
     * This method is mapped to the `PUT` endpoint and is used to update a user's information.
     *
     * @param id           the ID of the user
     * @param requestModel the request model containing the updated user information
     * @return a response entity with a status of `OK`
     * @throws ResponseStatusException if the user is not authenticated or if the user is not found or
     *                                 if the email format is incorrect
     */
    @PutMapping
    public ResponseEntity<String> updateUserById(@RequestParam Long id,
                                                 @RequestBody RegistrationRequestModel requestModel) {
        if (!authManager.checkUserAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }

        try {
            String username = userService.getUser(id).getName();
            if (!username.equals(authManager.getUsername())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
            }
            userService.updateUserById(id, requestModel);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        } catch (EmailFormatIncorrectException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_FORMAT_INCORRECT);
        } catch (UsernameFormatIncorrectException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_FORMAT_INCORRECT, e);
        }
    }

    /**
     * Retrieves the email for a user.
     * This method is mapped to the `GET /retrieveEmail` endpoint and is used to retrieve the email for a user.
     *
     * @param id the ID of the user
     * @return a response entity with the user's email and a status of `OK`
     * @throws ResponseStatusException if the user is not authenticated or if the user is not found
     */
    @GetMapping("/retrieveEmail")
    public ResponseEntity<Email> retrieveEmail(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id).getEmail());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        }
    }

    /**
     * Retrieves the name for a user.
     * This method is mapped to the `GET /retrieveName` endpoint and is used to retrieve the name for a user.
     *
     * @param id the ID of the user
     * @return a response entity with the user's name and a status of `OK`
     * @throws ResponseStatusException if the user is not authenticated or if the user is not found
     */
    @GetMapping("/retrieveName")
    public ResponseEntity<String> retrieveName(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id).getName());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        }
    }

    /**
     * Retrieves the gender for a user.
     * This method is mapped to the `GET /retrieveGender` endpoint and is used to retrieve the gender for a user.
     *
     * @param id the ID of the user
     * @return a response entity with the user's gender and a status of `OK`
     * @throws ResponseStatusException if the user is not authenticated or if the user is not found
     */
    @GetMapping("/retrieveGender")
    public ResponseEntity<Gender> retrieveGender(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id).getGender());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        }
    }

    /**
     * Retrieves the role preferences for a user.
     * This method is mapped to the `GET /retrieveRolePreferences` endpoint and is
     * used to retrieve the role preferences for a user.
     *
     * @param id the ID of the user
     * @return a response entity with the user's list of role preferences and a status of `OK`
     * @throws ResponseStatusException if the user is not authenticated or if the user is not found
     */
    @GetMapping("/retrieveRolePreferences")
    public ResponseEntity<List<RoleInfo>> retrieveRolePreferences(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id).getRoles());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        }
    }

    /**
     * Retrieves the schedule for a user.
     * This method is mapped to the `GET /retrieveUserSchedule` endpoint and is used to retrieve the schedule for a user.
     *
     * @param id the ID of the user
     * @return a response entity with the user's list of availabilities and a status of `OK`
     * @throws ResponseStatusException if the user is not authenticated or if the user is not found
     */
    @GetMapping("/retrieveUserSchedule")
    public ResponseEntity<List<Availability>> retrieveUserSchedule(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.getUser(id).getAvailabilities());
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        }
    }

    /**
     * Retrieves the user's data.
     * This method is mapped to the `GET /retrieveUserData` endpoint and is used to retrieve the user
     * specific information such as gender, availabilities, and preferred positions
     *
     * @param id the ID of the user
     * @return a response entity with a UserDataResponseModel DTO
     */
    @GetMapping("/retrieveUserData/{id}")
    public ResponseEntity<UserDataResponseModel> retrieveUserData(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(UserDataResponseModel.fromUser(userService.getUser(id)));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND, e);
        }
    }
}
