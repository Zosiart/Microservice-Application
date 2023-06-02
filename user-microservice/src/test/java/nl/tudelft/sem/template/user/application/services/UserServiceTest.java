package nl.tudelft.sem.template.user.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.enums.RoleEnum;
import nl.tudelft.sem.template.user.domain.enums.SkillLevelEnum;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.exceptions.MissingEmailException;
import nl.tudelft.sem.template.user.domain.exceptions.UserNotFoundException;
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.user.domain.models.RegistrationRequestModel;
import nl.tudelft.sem.template.user.domain.models.UserDataResponseModel;
import nl.tudelft.sem.template.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UserServiceTest {

    @Test
    public void createUserInvalidUsername() {
        RegistrationRequestModel model = new RegistrationRequestModel("vict",
                Gender.MALE, "victor@yahoo", new ArrayList<>(), new ArrayList<>(), Certificate.FOUR_PLUS, "Organization");
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.save(any(User.class))).thenReturn(null);
        UserService us = new UserService(userRepo);
        assertThrows(UsernameFormatIncorrectException.class, () -> us.createUser(model));
    }

    @Test
    public void createUserValidEmailAndUsername() throws UsernameFormatIncorrectException {
        RegistrationRequestModel model = new RegistrationRequestModel("victorLovesRowing",
            Gender.MALE, "victor@yahoo.com", new ArrayList<>(), new ArrayList<>(), Certificate.FOUR_PLUS,
            "Organization");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.save(any(User.class))).thenReturn(null);
        UserService us = new UserService(userRepo);

        us.createUser(model);
        verify(userRepo).save(userCaptor.capture());
        User actualUser = userCaptor.getValue();
        assertEquals("victorLovesRowing", actualUser.getName());
        assertEquals(Gender.MALE, actualUser.getGender());
        assertEquals("victor@yahoo.com", actualUser.getEmail().getEmailText());
    }

    @Test
    public void createUserInvalidEmail() throws UsernameFormatIncorrectException {
        RegistrationRequestModel model = new RegistrationRequestModel("victor4545",
            Gender.MALE, "victor@yahoo", new ArrayList<>(), new ArrayList<>(), Certificate.FOUR_PLUS, "Organization");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.save(any(User.class))).thenReturn(null);
        UserService us = new UserService(userRepo);
        us.createUser(model);
        verify(userRepo).save(userCaptor.capture());
        User actualUser = userCaptor.getValue();
        assertEquals("victor4545", actualUser.getName());
        assertEquals(Gender.MALE, actualUser.getGender());
        assertNull(actualUser.getEmail());
    }

    @Test
    public void updateUserInvalidUsername() {
        UserRepository userRepository = mock(UserRepository.class);
        User user = new User("popescu23", Gender.MALE);
        when(userRepository.existsUserById(25L)).thenReturn(true);
        when(userRepository.findUserById(25L)).thenReturn(java.util.Optional.of(user));
        RegistrationRequestModel model = new RegistrationRequestModel("!victor4545",
                Gender.MALE, "victor@yahoo", new ArrayList<>(), new ArrayList<>(), Certificate.FOUR_PLUS,
                "Organization");
        assertThrows(UsernameFormatIncorrectException.class, () -> new UserService(userRepository)
                .updateUserById(25L, model));
    }

    @Test
    public void updateUserEmptyFieldsUnchanged()
            throws EmailFormatIncorrectException, UsernameFormatIncorrectException, UserNotFoundException {
        UserRepository userRepository = mock(UserRepository.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 12, 28,
                10, 45, 0);
        Date start = calendar.getTime();
        calendar.set(2022, 12, 28, 14, 45, 0);
        Date end = calendar.getTime();
        List<RoleInfo> roles = List.of(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Amateur));
        List<Availability> availabilities = List.of(new Availability(start, end));
        User user = new User("rowingFan1999", new Email("example@yahoo.com"), Gender.MALE, roles,
                availabilities, Certificate.EIGHT_PLUS, "Laga");
        when(userRepository.existsUserById(25L)).thenReturn(true);
        when(userRepository.findUserById(25L)).thenReturn(java.util.Optional.of(user));
        RegistrationRequestModel model = new RegistrationRequestModel();
        assertThrows(UsernameFormatIncorrectException.class,
                () -> new UserService(userRepository).updateUserById(25L, model));
        assertEquals("rowingFan1999", user.getName());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals("example@yahoo.com", user.getEmail().getEmailText());
        assertEquals(roles, user.getRoles());
        assertEquals(availabilities, user.getAvailabilities());
        assertEquals(Certificate.EIGHT_PLUS, user.getCertificate());
        assertEquals("Laga", user.getOrganization());
    }

    @Test
    public void retrieveMessagingInfoUserNotFound() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(23L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> new UserService(userRepo).retrieveUserMessagingInformation(23L));
    }

    @Test
    public void retrieveMessagingInfoNoEmail() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsUserById(123L)).thenReturn(true);
        User user = new User("popescu23", Gender.MALE);
        when(userRepository.findUserById(123L)).thenReturn(java.util.Optional.of(user));
        UserService us = new UserService(userRepository);
        assertThrows(MissingEmailException.class, () -> us.retrieveUserMessagingInformation(123L));
    }

    @Test
    public void retrieveMessagingInfoSuccessful() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsUserById(123L)).thenReturn(true);
        User user = new User("popescu12345", new Email("sharkparadise@gmail.com"), Gender.MALE, null, null, null, null);
        when(userRepository.findUserById(123L)).thenReturn(java.util.Optional.of(user));
        MessagingInfoResponseModel response = new UserService(userRepository).retrieveUserMessagingInformation(123L);
        assertEquals("popescu12345", response.getName());
        assertEquals("sharkparadise@gmail.com", response.getEmail());
    }

    @Test
    public void updateUserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsUserById(123L)).thenReturn(false);
        assertThrows(UserNotFoundException.class,
            () -> new UserService(userRepository).updateUserById(111L, new RegistrationRequestModel()));
    }

    @Test
    public void updateUserAllFields() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsUserById(123L)).thenReturn(true);
        List<RoleInfo> roles = new ArrayList<>();
        roles.add(new RoleInfo(RoleEnum.StarboardSideRower, SkillLevelEnum.Amateur));
        roles.add(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Competitive));

        List<Availability> schedule = new ArrayList<>();
        schedule.add(new Availability(new Date(2023, 1, 24), new Date(2023, 1, 25)));
        RegistrationRequestModel model = new RegistrationRequestModel("ABC12345678",
            Gender.NON_BINARY, "abc@gmail.com",
            roles, schedule, Certificate.EIGHT_PLUS, "Organization");
        User user = new User("ABC87654321", new Email("abc@yahoo.com"), Gender.MALE, null, null, null, null);

        when(userRepository.findUserById(123L)).thenReturn(java.util.Optional.of(user));
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        UserService userService = new UserService(userRepository);
        userService.updateUserById(123L, model);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("ABC12345678", savedUser.getName());
        assertEquals(Gender.NON_BINARY, savedUser.getGender());
        assertEquals("abc@gmail.com", savedUser.getEmail().getEmailText());
        assertEquals(Certificate.EIGHT_PLUS, savedUser.getCertificate());
        assertEquals(schedule, savedUser.getAvailabilities());
        assertEquals(roles, savedUser.getRoles());
    }

    @Test
    public void getEmailUserNotFound() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(23L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> new UserService(userRepo).getUser(23L).getEmail());
    }

    @Test
    public void getEmailSuccessful() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(500L)).thenReturn(true);

        when(userRepo.findUserById(500L)).thenReturn(
            java.util.Optional.of(new User("basicUsername123", new Email("abc@yahoo.com"), Gender.MALE,
                    null, null, null, null)));

        UserService service = new UserService(userRepo);
        assertEquals("abc@yahoo.com", service.getUser(500L).getEmail().getEmailText());
    }

    @Test
    public void getNameUserNotFound() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(23L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> new UserService(userRepo).getUser(23L).getName());
    }

    @Test
    public void getNameSuccessful() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(500L)).thenReturn(true);

        when(userRepo.findUserById(500L)).thenReturn(
            java.util.Optional.of(new User("SaraStarboard", new Email("abc@yahoo.com"), Gender.MALE,
                    null, null, null, null)));

        UserService service = new UserService(userRepo);
        assertEquals("SaraStarboard", service.getUser(500L).getName());
    }

    @Test
    public void getGenderUserNotFound() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(144L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> new UserService(userRepo).getUser(144L).getGender());
    }

    @Test
    public void getGenderSuccessful() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(500L)).thenReturn(true);

        when(userRepo.findUserById(500L)).thenReturn(
            java.util.Optional.of(new User("basicusername", new Email("abc@yahoo.com"), Gender.MALE,
                    null, null, null, null)));

        UserService service = new UserService(userRepo);
        assertEquals(Gender.MALE, service.getUser(500L).getGender());
    }

    @Test
    public void getRolesUserNotFound() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(888L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> new UserService(userRepo).getUser(888L).getRoles());
    }

    @Test
    public void getRolesSuccessful() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(500L)).thenReturn(true);
        List<RoleInfo> roles = new ArrayList<>();
        roles.add(new RoleInfo(RoleEnum.Cox, SkillLevelEnum.Competitive));
        roles.add(new RoleInfo(RoleEnum.ScullingRower, SkillLevelEnum.Competitive));
        when(userRepo.findUserById(500L)).thenReturn(
            java.util.Optional.of(new User("basicusername123", new Email("abc@yahoo.com"), Gender.MALE,
                    roles, null, null, null)));

        UserService service = new UserService(userRepo);
        assertEquals(roles, service.getUser(500L).getRoles());
    }

    @Test
    public void getScheduleUserNotFound() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(888L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> new UserService(userRepo).getUser(888L).getAvailabilities());
    }

    @Test
    public void getScheduleSuccessful() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(500L)).thenReturn(true);
        List<Availability> schedule = new ArrayList<>();
        schedule.add(new Availability(new Date(2023, 1, 24), new Date(2023, 1, 25)));
        when(userRepo.findUserById(500L)).thenReturn(
            java.util.Optional.of(new User("basicUsername123", new Email("abc@yahoo.com"), Gender.MALE,
                    null, schedule, null, null)));
        UserService service = new UserService(userRepo);
        assertEquals(schedule, service.getUser(500L).getAvailabilities());
    }

    @Test
    public void getDataUserNotFound() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(23L)).thenReturn(false);
        assertThrows(UserNotFoundException.class,
                () -> UserDataResponseModel.fromUser(new UserService(userRepo).getUser(23L)));
    }

    @Test
    public void getDataSuccessful() throws Exception {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsUserById(500L)).thenReturn(true);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 12, 28, 10, 45, 0);
        Date start = calendar.getTime();
        calendar.set(2022, 12, 28, 14, 45, 0);
        Date end = calendar.getTime();
        List<RoleInfo> roles = List.of(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Amateur));
        List<Availability> availabilities = List.of(new Availability(start, end));
        User user = new User("rowingFan1999", new Email("example@yahoo.com"), Gender.MALE, roles, availabilities,
                Certificate.EIGHT_PLUS, "Laga");
        when(userRepo.findUserById(500L)).thenReturn(
                java.util.Optional.of(user));
        UserDataResponseModel model = UserDataResponseModel.fromUser(new UserService(userRepo).getUser(500L));

        assertEquals(availabilities, model.getAvailabilities());
        assertEquals(roles, model.getRoles());
        assertEquals(Certificate.EIGHT_PLUS, model.getCertificate());
        assertEquals(Gender.MALE, model.getGender());
        assertEquals("Laga", model.getOrganization());
    }
}