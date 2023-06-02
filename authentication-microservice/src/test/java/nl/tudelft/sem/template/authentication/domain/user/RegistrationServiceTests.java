package nl.tudelft.sem.template.authentication.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import nl.tudelft.sem.template.authentication.domain.user.entity.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.entity.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.exception.PasswordTooWeak;
import nl.tudelft.sem.template.authentication.domain.user.exception.UserNameAlreadyInUse;
import nl.tudelft.sem.template.authentication.domain.user.repository.UserRepository;
import nl.tudelft.sem.template.authentication.domain.user.service.PasswordHashingService;
import nl.tudelft.sem.template.authentication.domain.user.service.RegistrationService;
import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest()
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegistrationServiceTests {

    @Autowired
    private transient RegistrationService registrationService;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    @Autowired
    private transient UserRepository userRepository;

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void createUser_withValidData_worksCorrectly() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final String testPassword = "Password123@";
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        // Act
        mockServer.expect(ExpectedCount.manyTimes(), requestTo("http://localhost:8082/api/user/register"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(String.valueOf(1L)));

        RegistrationRequestModel registrationRequestModel = new RegistrationRequestModel();
        registrationRequestModel.setUsername(testUser);
        registrationRequestModel.setPassword(testPassword);
        registrationService.registerUser(registrationRequestModel);

        // Assert
        AppUser savedUser = userRepository.findByUsername(testUser).orElseThrow();

        assertThat(savedUser.getUsername()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
    }

    @Test
    public void createUser_withExistingUser_throwsException() {
        // Arrange
        final String testUser = "SomeUser";
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final String newTestPassword = "Password123@";

        AppUser existingAppUser = new AppUser(testUser, existingTestPassword);
        userRepository.save(existingAppUser);

        // Act
        RegistrationRequestModel registrationRequestModel = new RegistrationRequestModel();
        registrationRequestModel.setUsername(testUser);
        registrationRequestModel.setPassword(newTestPassword);
        ThrowingCallable action = () -> registrationService.registerUser(registrationRequestModel);

        // Assert
        assertThatExceptionOfType(UserNameAlreadyInUse.class)
                .isThrownBy(action);

        AppUser savedUser = userRepository.findByUsername(testUser).orElseThrow();

        assertThat(savedUser.getUsername()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(existingTestPassword);
    }

    @Test
    public void createUser_withTooWeakPassword_throwsException() {
        final String testUser = "SomeUser";
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final String newTestPassword = "password123@";

        RegistrationRequestModel registrationRequestModel = new RegistrationRequestModel();
        registrationRequestModel.setUsername(testUser);
        registrationRequestModel.setPassword(newTestPassword);
        ThrowingCallable action = () -> registrationService.registerUser(registrationRequestModel);

        // Assert
        assertThatExceptionOfType(PasswordTooWeak.class)
                .isThrownBy(action);

        AppUser savedUser = userRepository.findByUsername(testUser).orElse(null);

        assertThat(savedUser).isNull();
    }
}
