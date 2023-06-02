package nl.tudelft.sem.template.user.application.services;


import java.util.List;
import lombok.NonNull;
import nl.tudelft.sem.template.user.domain.BuilderDirector;
import nl.tudelft.sem.template.user.domain.UserBuilder;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.exceptions.MissingEmailException;
import nl.tudelft.sem.template.user.domain.exceptions.UserNotFoundException;
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.user.domain.models.RegistrationRequestModel;
import nl.tudelft.sem.template.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final transient BuilderDirector director;
    private final transient UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.director = new BuilderDirector();
    }

    /**
     * Creates a new user based on the given {@code registrationRequestModel}.
     * If an {@link EmailFormatIncorrectException} is thrown while creating the user,
     * a basic user with only a name and gender is created instead.
     *
     * @param registrationRequestModel the model containing information about the user to be created
     * @return the unique identifier for the newly created user
     */
    public Long createUser(@NonNull RegistrationRequestModel registrationRequestModel)
            throws UsernameFormatIncorrectException {
        try {
            return createCompleteUser(registrationRequestModel);
        } catch (EmailFormatIncorrectException e) {
            User userConstructed = director.constructBasicUser(new UserBuilder(), registrationRequestModel.getName(),
                    registrationRequestModel.getGenderEnum());
            userRepository.save(userConstructed);
            return userConstructed.getId();
        }
    }


    /**
     * Creates a new complete user with the given information.
     * A complete user has a name, gender, email, role information, availabilities, and certificate.
     *
     * @param registrationRequestModel - the model containing information about the user to be created
     * @return the unique identifier for the newly created user
     * @throws EmailFormatIncorrectException if the provided email is not a valid format
     */
    private Long createCompleteUser(@NonNull RegistrationRequestModel registrationRequestModel)
            throws EmailFormatIncorrectException, UsernameFormatIncorrectException {
        if (!User.validUsername(registrationRequestModel.getName())) {
            throw new UsernameFormatIncorrectException();
        }

        Email email = new Email(registrationRequestModel.getEmail());
        User userConstructed = director.constructCompleteUser(new UserBuilder(), registrationRequestModel.getName(),
                registrationRequestModel.getGenderEnum(), email, registrationRequestModel.getRoleInfoList(),
                registrationRequestModel.getAvailabilities(), registrationRequestModel.getCertificate(),
                registrationRequestModel.getOrganization()
        );

        userRepository.save(userConstructed);
        return userConstructed.getId();
    }

    /**
     * Retrieves the messaging information (name and email) of the user with the given unique identifier.
     *
     * @param id the unique identifier of the user to retrieve messaging information for
     * @return a model containing the messaging information for the user
     * @throws UserNotFoundException if no user with the given unique identifier exists
     * @throws MissingEmailException if the user with the given unique identifier has not registered an email yet
     */
    public MessagingInfoResponseModel retrieveUserMessagingInformation(@NonNull Long id)
        throws UserNotFoundException, MissingEmailException {
        User userFound = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (userFound.getEmail().getEmailText() == null) {
            throw new MissingEmailException("User with id " + id + " hasn't registered yet his email.");
        }

        return new MessagingInfoResponseModel(userFound.getName(), userFound.getEmail().getEmailText());
    }

    /**
     * Updates the user with the given unique identifier using the information in the given {@code requestModel}.
     * Only the fields in the {@code requestModel} that are not {@code null} will be updated in the user.
     *
     * @param id           the unique identifier of the user to update
     * @param requestModel the model containing the updated information for the user
     * @throws UserNotFoundException         if no user with the given unique identifier exists
     * @throws EmailFormatIncorrectException if the email provided in the {@code requestModel} is not a valid format
     */
    public void updateUserById(@NonNull Long id, @NonNull RegistrationRequestModel requestModel)
        throws UserNotFoundException, EmailFormatIncorrectException, UsernameFormatIncorrectException {
        User user = userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!User.validUsername(requestModel.getName())) {
            throw new UsernameFormatIncorrectException();
        }
        user.setName(requestModel.getName());

        user.setGender(requestModel.getGenderEnum());
        if (requestModel.getEmail() != null) {
            user.setEmail(new Email(requestModel.getEmail()));
        }

        if (requestModel.getRoleInfoList() != null) {
            user.setRoles(requestModel.getRoleInfoList());
        }

        if (requestModel.getAvailabilities() != null) {
            user.setAvailabilities(requestModel.getAvailabilities());
        }

        if (requestModel.getCertificate() != null) {
            user.setCertificate(requestModel.getCertificate());
        }

        if (requestModel.getOrganization() != null) {
            user.setOrganization(requestModel.getOrganization());
        }

        userRepository.save(user);
    }


    /**
     * Retrieves all user data.
     *
     * @param id - the unique identifier of the user to retrieve the data for
     * @return the user data
     * @throws UserNotFoundException - if no user with the given unique identifier exists
     */
    public User getUser(@NonNull Long id) throws UserNotFoundException {
        return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
