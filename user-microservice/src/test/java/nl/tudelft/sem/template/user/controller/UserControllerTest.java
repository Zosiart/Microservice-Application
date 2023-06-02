package nl.tudelft.sem.template.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.user.application.services.UserService;
import nl.tudelft.sem.template.user.authentication.AuthManager;
import nl.tudelft.sem.template.user.authentication.JwtTokenVerifier;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Mock
    private UserService service;

    @Test
    public void registerTest() throws UsernameFormatIncorrectException {
        UserController uc = new UserController(service, mockAuthenticationManager);
        RegistrationRequestModel model = new RegistrationRequestModel("username",
                Gender.NON_BINARY, "email@yahoo.com", null, null,
                null, null);
        ResponseEntity<Long> response = uc.register(model);
        verify(service).createUser(model);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), 0);
    }

    @Test
    public void registerIncorrectUsername() {
        UserController uc = new UserController(service,
                mockAuthenticationManager);
        assertThrows(ResponseStatusException.class,
                () -> uc.register(new RegistrationRequestModel("a", Gender.NON_BINARY,
                        "email@yahoo.com", null, null, null, null)));
    }


    @Test
    public void messagingInfoUserNotFound() throws Exception {
        when(service.retrieveUserMessagingInformation(0L)).thenThrow(new UserNotFoundException(0L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> uc.retrieveMessagingInformation(0L));

        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void messagingInfoNoEmail() throws Exception {
        when(service.retrieveUserMessagingInformation(0L)).thenThrow(new MissingEmailException(""));
        UserController uc = new UserController(service, mockAuthenticationManager);

        ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> uc.retrieveMessagingInformation(0L));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_MISSING_EMAIL\""));
    }

    @Test
    public void messagingInfoSuccessful() throws Exception {
        MessagingInfoResponseModel model = new MessagingInfoResponseModel("abc", "abc@yahoo.com");
        when(service.retrieveUserMessagingInformation(0L)).thenReturn(model);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseEntity<MessagingInfoResponseModel> response = uc.retrieveMessagingInformation(0L);
        assertEquals(response.getBody(), model);
    }


    @Test
    public void updateUnauthorized() {
        RegistrationRequestModel model = new RegistrationRequestModel();
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(false);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.updateUserById(0L, model));
        assertEquals(exception.getMessage(), "401 UNAUTHORIZED \"User is not authenticated.\"");
    }

    @Test
    public void updateUserNotFound() throws Exception {
        RegistrationRequestModel model = new RegistrationRequestModel();
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(service.getUser(0L)).thenThrow(new UserNotFoundException(0L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.updateUserById(0L, model));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void updateUserIncorrectEmail() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(mockAuthenticationManager.getUsername()).thenReturn("username");
        User user = new User();
        user.setName("username");
        when(service.getUser(0L)).thenReturn(user);
        RegistrationRequestModel model = new RegistrationRequestModel();
        doThrow(new EmailFormatIncorrectException("")).when(service).updateUserById(0L, model);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.updateUserById(0L, model));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"EMAIL_FORMAT_INCORRECT\""));
    }

    @Test
    public void updateAnotherUserNotAllowed() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(mockAuthenticationManager.getUsername()).thenReturn("username");
        User user = new User();
        user.setName("username123");
        when(service.getUser(0L)).thenReturn(user);
        RegistrationRequestModel model = new RegistrationRequestModel();
        doThrow(new EmailFormatIncorrectException("")).when(service).updateUserById(0L, model);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.updateUserById(0L, model));
        assertEquals(exception.getMessage(), "401 UNAUTHORIZED \"User is not authenticated.\"");
    }    

    @Test    
    public void updateUserIncorrectUsername() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(mockAuthenticationManager.getUsername()).thenReturn("abcdef123");
        User user = new User();
        user.setName("abcdef123");
        when(service.getUser(0L)).thenReturn(user);
        RegistrationRequestModel model = new RegistrationRequestModel("!abd", null, null,
                null, null, null, null);
        doThrow(new UsernameFormatIncorrectException("")).when(service).updateUserById(0L, model);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.updateUserById(0L,
                model));
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USERNAME_FORMAT_INCORRECT\""));
    }

    @Test
    public void updateSuccessful() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(mockAuthenticationManager.getUsername()).thenReturn("username");
        User user = new User();
        user.setName("username");
        when(service.getUser(0L)).thenReturn(user);

        UserController uc = new UserController(service, mockAuthenticationManager);
        RegistrationRequestModel model = new RegistrationRequestModel();
        ResponseEntity<String> response = uc.updateUserById(0L, model);

        verify(service).updateUserById(0L, model);
        assertEquals(response, ResponseEntity.ok().build());
    }

    @Test
    public void retrieveEmailUserNotFound() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(service.getUser(0L)).thenThrow(new UserNotFoundException(0L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.retrieveEmail(0L));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void retrieveEmailSuccessful() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        User user = new User();
        Email email = new Email("example@yahoo.com");
        user.setEmail(email);
        when(service.getUser(0L)).thenReturn(user);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseEntity<Email> response = uc.retrieveEmail(0L);
        assertEquals(response, ResponseEntity.ok(email));
    }

    @Test
    public void retrieveNameUserNotFound() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(service.getUser(0L)).thenThrow(new UserNotFoundException(0L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.retrieveName(0L));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void retrieveNameSuccessful() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        User user = new User();
        String name = "ABCDDEFGHI";
        user.setName(name);
        when(service.getUser(0L)).thenReturn(user);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseEntity<String> response = uc.retrieveName(0L);
        assertEquals(response, ResponseEntity.ok(name));
    }

    @Test
    public void retrieveGenderUserNotFound() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(service.getUser(0L)).thenThrow(new UserNotFoundException(0L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.retrieveGender(0L));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void retrieveGenderSuccessful() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        User user = new User();
        Gender gender = Gender.FEMALE;
        user.setGender(gender);
        when(service.getUser(0L)).thenReturn(user);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseEntity<Gender> response = uc.retrieveGender(0L);
        assertEquals(response, ResponseEntity.ok(gender));
    }

    @Test
    public void retrieveRolesUserNotFound() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(service.getUser(0L)).thenThrow(new UserNotFoundException(0L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> uc.retrieveRolePreferences(0L));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void retrieveRolesSuccessful() throws Exception {
        List<RoleInfo> roles = new ArrayList<>();
        roles.add(new RoleInfo(RoleEnum.ScullingRower, SkillLevelEnum.Amateur));
        User user = new User();
        user.setRoles(roles);
        when(service.getUser(0L)).thenReturn(user);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseEntity<List<RoleInfo>> response = uc.retrieveRolePreferences(0L);
        assertEquals(response, ResponseEntity.ok(roles));
    }

    @Test
    public void retrieveScheduleUserNotFound() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(service.getUser(0L)).thenThrow(new UserNotFoundException(0L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> uc.retrieveUserSchedule(0L));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void retrieveScheduleSuccessful() throws Exception {
        List<Availability> schedule = new ArrayList<>();
        schedule.add(new Availability(new Date(2022, 12, 19), new Date(2022, 12, 23)));
        User user = new User();
        user.setAvailabilities(schedule);
        when(service.getUser(0L)).thenReturn(user);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseEntity<List<Availability>> response = uc.retrieveUserSchedule(0L);
        assertEquals(response, ResponseEntity.ok(schedule));
    }

    @Test
    public void retrieveDataUserNotFound() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated()).thenReturn(true);
        when(service.getUser(27L)).thenThrow(new UserNotFoundException(27L));
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> uc.retrieveUserData(27L));
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST \"USER_NOT_FOUND\""));
    }

    @Test
    public void retrieveUserDataSuccessful() throws Exception {
        when(mockAuthenticationManager.checkUserAuthenticated())
                .thenReturn(true);
        User user = new User();
        user.setId(4L);
        user.setRoles(new ArrayList<>());
        user.setAvailabilities(new ArrayList<>());
        user.setCertificate(Certificate.EIGHT_PLUS);
        user.setGender(Gender.FEMALE);
        user.setOrganization("RowingOrg");
        UserDataResponseModel userDataResponseModel = new UserDataResponseModel(4L, new ArrayList<>(),
                new ArrayList<>(), Certificate.EIGHT_PLUS, Gender.FEMALE, "RowingOrg");
        when(service.getUser(4L)).thenReturn(user);
        UserController uc = new UserController(service, mockAuthenticationManager);
        ResponseEntity<UserDataResponseModel> response = uc.retrieveUserData(4L);
        assertEquals(response, ResponseEntity.ok(userDataResponseModel));
    }
}