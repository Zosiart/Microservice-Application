package nl.tudelft.sem.template.planning.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import nl.tudelft.sem.template.planning.application.client.MessageManager;
import nl.tudelft.sem.template.planning.application.client.UserManager;
import nl.tudelft.sem.template.planning.application.planner.RegisterForActivityListener;
import nl.tudelft.sem.template.planning.authentication.AuthManager;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;
import nl.tudelft.sem.template.planning.domain.entity.Availability;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.entity.RoleRequirements;
import nl.tudelft.sem.template.planning.domain.enums.*;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.exception.InsufficientPermissions;
import nl.tudelft.sem.template.planning.exception.ServiceNotAvailable;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;
import nl.tudelft.sem.template.planning.utils.ActivityRequestBuilder;
import nl.tudelft.sem.template.planning.utils.UserRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest()
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockAuthenticationManager", "userRequestBuilder", "activityRequestBuilder", "messageManager",
    "registerForActivityListener", "activityManager", "userManager", "activityUtils"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlanningServiceTest {
    @Autowired
    private transient ActivityRequestBuilder activityRequestBuilder;

    @Autowired
    private transient UserRequestBuilder userRequestBuilder;

    @Autowired
    private transient PlanningService planningService;

    @Autowired
    private transient AppointmentRepository appointmentRepository;

    @Autowired
    private transient AuthManager authManager;

    @Autowired
    private transient UserManager userManager;

    @Autowired
    private transient ActivityManager activityManager;

    @Autowired
    private transient ActivityUtils activityUtils;

    @Autowired
    private transient MessageManager messageManager;

    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private transient RegisterForActivityListener registerForActivityListener;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testCreateAppointmentSuccessfully() throws InsufficientPermissions, ServiceNotAvailable {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(false);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);


        planningService.createAppointment(1, Role.PortSideRower);
        assertEquals(appointmentRepository.count(), 1);
    }

    @Test
    public void testCreateAppointmentRequiredRole() {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);

        InsufficientPermissions exception = assertThrows(InsufficientPermissions.class, () -> {
            planningService.createAppointment(1, Role.PortSideRower);
        });
    }

    @Test
    public void testCreateAppointmentInsufficientLevel() {
        when(authManager.getUserId()).thenReturn("1");

        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 5)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));
        appointmentRepository.save(new Appointment(1, 1, Role.PortSideRower));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Amateur)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);

        InsufficientPermissions exception = assertThrows(InsufficientPermissions.class, () -> {
            planningService.createAppointment(1, Role.PortSideRower);
        });
    }

    @Test
    public void testCreateAppointmentRoleNotPresent() {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(false);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);

        InsufficientPermissions exception = assertThrows(InsufficientPermissions.class, () -> {
            planningService.createAppointment(1, Role.Cox);
        });
    }

    @Test
    public void testCreateAppointmentNoSpots() {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(false);
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));
        Appointment appointment = new Appointment(1, 2, Role.PortSideRower);
        appointment.setStatus(Status.ACCEPTED);
        appointmentRepository.save(appointment);

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);

        InsufficientPermissions exception = assertThrows(InsufficientPermissions.class, () -> {
            planningService.createAppointment(1, Role.PortSideRower);
        });
    }

    @Test
    public void testCreateAppointmentAlreadyApplied() {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(false);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 2)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));
        appointmentRepository.save(new Appointment(1, 1, Role.PortSideRower));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);

        InsufficientPermissions exception = assertThrows(InsufficientPermissions.class, () -> {
            planningService.createAppointment(1, Role.PortSideRower);
        });
    }

    @Test
    public void testResolveAppointmentSuccessfully() throws InsufficientPermissions, ServiceNotAvailable {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setUserId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setGender(Gender.FEMALE);
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 5)));

        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(2L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData(anyLong())).thenReturn(userResponseModel);
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);
        when(activityUtils.verifyIfUserIsAllowedToChangeStatus(anyLong())).thenReturn(true);
        appointmentRepository.save(new Appointment(1, 2, Role.PortSideRower));
        assertTrue(planningService.resolveAppointment(1, Status.ACCEPTED));
        assertEquals(Status.ACCEPTED, appointmentRepository.findById(1).get().getStatus());
    }

    @Test
    public void testResolveAppointmentNoSuchAppointment() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            planningService.resolveAppointment(1, Status.ACCEPTED);
        });
    }

    @Test
    public void testResolveAppointmentSettingPending() throws InsufficientPermissions, ServiceNotAvailable {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);

        appointmentRepository.save(new Appointment(1, 2, Role.PortSideRower));
        assertFalse(planningService.resolveAppointment(1, Status.PENDING));
    }

    @Test
    public void testResolveAppointmentAppointmentNotPending() throws InsufficientPermissions, ServiceNotAvailable {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);


        Appointment appointment = new Appointment(1, 2, Role.PortSideRower);
        appointment.setStatus(Status.ACCEPTED);
        appointmentRepository.save(appointment);
        assertFalse(planningService.resolveAppointment(1, Status.ACCEPTED));
    }

    @Test
    public void testResolveAppointmentNotOwner() throws InsufficientPermissions {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setUserId(2);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel).thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData()).thenReturn(userResponseModel);

        appointmentRepository.save(new Appointment(1, 2, Role.PortSideRower));

        InsufficientPermissions exception = assertThrows(InsufficientPermissions.class, () -> {
            planningService.resolveAppointment(1, Status.ACCEPTED);
        });
    }

    @Test
    public void getAllAppointments() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        ActivityResponseModel activityResponseModel2 = new ActivityResponseModel();
        activityResponseModel2.setId(2);
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel2);

        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel)
                .thenReturn(messagingInfoResponseModel);

        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS))
                .thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        when(authManager.getUserId()).thenReturn("1").thenReturn("1");

        appointmentRepository.save(new Appointment(1, 1, Role.PortSideRower));
        appointmentRepository.save(new Appointment(2, 1, Role.PortSideRower));

        assertEquals(2, planningService.getAllAppointments().size());
    }

    @Test
    public void getAllAppointmentsNoAppointments() {
        when(authManager.getUserId()).thenReturn("1");

        assertEquals(0, planningService.getAllAppointments().size());
    }

    @Test
    public void getAppointmentSuccessfully() throws InsufficientPermissions, ServiceNotAvailable {
        when(authManager.getUserId()).thenReturn("1");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(false);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 2)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));
        appointmentRepository.save(new Appointment(1, 1, Role.PortSideRower));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData(anyLong())).thenReturn(userResponseModel);

        assertNotNull(planningService.getAppointment(1));
    }

    @Test
    public void getAppointmentDoesNotExist() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            planningService.getAppointment(1);
        });
    }

    @Test
    public void getAppointmentUserCannotAccess() {
        when(authManager.getUserId()).thenReturn("2");
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(false);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 2)));
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel)
                .thenReturn(activityResponseModel);
        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel);
        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));
        appointmentRepository.save(new Appointment(1, 1, Role.PortSideRower));

        UserDataResponseModel userResponseModel = new UserDataResponseModel(2L,
                List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
                Gender.FEMALE, "Laga");
        when(userManager.retrieveUserData(anyLong())).thenReturn(userResponseModel);

        InsufficientPermissions exception = assertThrows(InsufficientPermissions.class, () -> {
            planningService.getAppointment(1);
        });
    }

    @Test
    public void getAllPendingAppointments() throws ServiceNotAvailable {
        when(authManager.getUserId()).thenReturn("1");

        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        ActivityResponseModel activityResponseModel1 = new ActivityResponseModel();
        activityResponseModel1.setId(2);
        ActivityResponseModel[] arr = {activityResponseModel, activityResponseModel1};
        when(activityRequestBuilder.withOwner(1)).thenReturn(activityRequestBuilder);
        when(activityRequestBuilder.get())
                .thenReturn(ResponseEntity.ok(arr));

        ActivityResponseModel activityResponseModel3 = new ActivityResponseModel();
        activityResponseModel.setId(1);
        ActivityResponseModel activityResponseModel2 = new ActivityResponseModel();
        activityResponseModel2.setId(2);
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel3)
                .thenReturn(activityResponseModel2);

        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel)
                .thenReturn(messagingInfoResponseModel);

        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS))
                .thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        appointmentRepository.save(new Appointment(1, 1, Role.PortSideRower));
        appointmentRepository.save(new Appointment(2, 2, Role.PortSideRower));

        when(activityManager.retrieveOwnedActivities()).thenReturn(List.of(activityResponseModel2,
                activityResponseModel3));

        assertEquals(1, planningService.getPendingAppointments().size());
    }

    @Test
    public void getAllPendingAppointmentsSkipNotPending() throws ServiceNotAvailable {
        when(authManager.getUserId()).thenReturn("1");

        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        ActivityResponseModel activityResponseModel1 = new ActivityResponseModel();
        activityResponseModel1.setId(2);
        ActivityResponseModel[] arr = {activityResponseModel, activityResponseModel1};
        when(activityRequestBuilder.withOwner(1)).thenReturn(activityRequestBuilder);
        when(activityRequestBuilder.get())
                .thenReturn(ResponseEntity.ok(arr));

        ActivityResponseModel activityResponseModel3 = new ActivityResponseModel();
        activityResponseModel.setId(1);
        ActivityResponseModel activityResponseModel2 = new ActivityResponseModel();
        activityResponseModel2.setId(2);
        when(activityManager.retrieveActivity(anyLong())).thenReturn(activityResponseModel3)
                .thenReturn(activityResponseModel2);

        MessagingInfoResponseModel messagingInfoResponseModel = new MessagingInfoResponseModel();
        messagingInfoResponseModel.setEmail("email@email.com");
        when(userManager.retrieveUserMessagingInformation(anyLong())).thenReturn(messagingInfoResponseModel)
                .thenReturn(messagingInfoResponseModel);

        when(messageManager.sendMessage(any())).thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS))
                .thenReturn(ResponseEntity.ok(MessageStatus.SUCCESS));

        appointmentRepository.save(new Appointment(1, 1, Role.PortSideRower));
        Appointment appointment = new Appointment(2, 1, Role.PortSideRower);
        appointment.setStatus(Status.ACCEPTED);
        appointmentRepository.save(appointment);

        when(activityManager.retrieveOwnedActivities()).thenReturn(List.of(activityResponseModel2,
                activityResponseModel3));

        assertEquals(0, planningService.getPendingAppointments().size());
    }

    @Test
    public void getAvailableActivitiesFilterTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart1 = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd1 = calendar.getTime();
        calendar.set(2024, 4, 11, 0, 0, 0);
        Date userStart2 = calendar.getTime();
        calendar.set(2024, 4, 12, 0, 0, 0);
        Date userEnd2 = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(
                new RoleInfo(Role.Cox, Level.Competitive),
                new RoleInfo(Role.PortSideRower, Level.Competitive));
        List<Availability> availabilities = List.of(
                new Availability(userStart1, userEnd1),
                new Availability(userStart2, userEnd2));
        UserDataResponseModel user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");
        when(userManager.retrieveUserData()).thenReturn(user);

        List<RoleRequirements> roleRequirements1 = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 4, 11,
                12, 0, 0);
        Date activity1Start = calendar.getTime();
        calendar.set(2024, 4, 11, 17, 0, 0);
        Date activity1End = calendar.getTime();
        ActivityResponseModel notEligible = new ActivityResponseModel(1L, 2L, activity1Start, activity1End,
                "test", Certificate.FOUR_PLUS, roleRequirements1, Gender.FEMALE, "TU Delft",
                Level.Competitive, true);

        List<RoleRequirements> roleRequirements2 = List.of(new RoleRequirements(Role.PortSideRower, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activity2Start = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activity2End = calendar.getTime();
        ActivityResponseModel eligible = new ActivityResponseModel(2L, 2L, activity2Start, activity2End,
                "test", Certificate.FOUR_PLUS, roleRequirements2, Gender.FEMALE, "TU Delft",
                Level.Competitive, true);
        when(activityManager.retrieveEligibleActivities(user)).thenReturn(List.of(notEligible, eligible));

        Map<Long, ActivityResponseModel> activity = new HashMap<>();
        activity.put(0L, notEligible);
        when(activityUtils.getActivitiesWithStatus(user.getId(), Status.ACCEPTED)).thenReturn(activity);
        assertEquals(List.of(eligible), planningService.getAvailableActivities());
    }
}
