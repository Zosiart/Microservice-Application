package nl.tudelft.sem.template.planning.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.planning.domain.entity.Availability;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.entity.RoleRequirements;
import nl.tudelft.sem.template.planning.domain.enums.*;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;
import org.junit.jupiter.api.Test;

public class UserEligibilityForActivityTest {

    private UserDataResponseModel user;
    private ActivityResponseModel activity;
    private AppointmentRepository appointmentRepo;

    @Test
    public void checkUserAccepted() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(new RoleInfo(Role.Cox, Level.Competitive));
        List<Availability> availabilities = List.of(new Availability(userStart, userEnd));
        user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");

        List<RoleRequirements> roleRequirements = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activityStart = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activityEnd = calendar.getTime();
        activity = new ActivityResponseModel(1L, 2L, activityStart, activityEnd, "test",
                Certificate.FOUR_PLUS, roleRequirements, Gender.FEMALE, "TU Delft", Level.Competitive, true);
        appointmentRepo = mock(AppointmentRepository.class);
        when(appointmentRepo.countStatusForActivityRole(1L, Role.Cox, Status.ACCEPTED)).thenReturn(0);
        assertTrue(UserEligibilityForActivity.canUserApply(user, activity, Role.Cox, false,
                new ArrayList<>(), appointmentRepo));

    }

    @Test
    public void checkTimeFalseTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(new RoleInfo(Role.Cox, Level.Competitive));
        List<Availability> availabilities = List.of(new Availability(userStart, userEnd));
        user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");

        List<RoleRequirements> roleRequirements = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activityStart = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activityEnd = calendar.getTime();
        activity = new ActivityResponseModel(1L, 2L, activityStart, activityEnd, "test",
                Certificate.FOUR_PLUS, roleRequirements, Gender.FEMALE, "TU Delft", Level.Competitive, true);
        appointmentRepo = mock(AppointmentRepository.class);
        when(appointmentRepo.countStatusForActivityRole(1L, Role.Cox, Status.ACCEPTED)).thenReturn(0);
        calendar = Calendar.getInstance();
        calendar.set(2020, 2, 11,
                11, 0, 0);
        Date start = calendar.getTime();
        calendar.set(2020, 2, 1, 17, 0, 0);
        Date end = calendar.getTime();
        activity.setStartingDate(start);
        activity.setEndingDate(end);
        assertFalse(UserEligibilityForActivity.canUserApply(user, activity, Role.Cox, false,
                new ArrayList<>(), appointmentRepo));
    }

    @Test
    public void checkLevelFalseTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(new RoleInfo(Role.Cox, Level.Competitive));
        List<Availability> availabilities = List.of(new Availability(userStart, userEnd));
        user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");

        List<RoleRequirements> roleRequirements = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activityStart = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activityEnd = calendar.getTime();
        activity = new ActivityResponseModel(1L, 2L, activityStart, activityEnd, "test",
                Certificate.FOUR_PLUS, roleRequirements, Gender.FEMALE, "TU Delft", Level.Competitive, true);
        appointmentRepo = mock(AppointmentRepository.class);
        user.setRoles(List.of(new RoleInfo(Role.Cox, Level.Amateur)));
        assertFalse(UserEligibilityForActivity.canUserApply(user, activity, Role.Cox, false,
                new ArrayList<>(), appointmentRepo));
    }

    @Test
    public void checkRoleFalseTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(new RoleInfo(Role.Cox, Level.Competitive));
        List<Availability> availabilities = List.of(new Availability(userStart, userEnd));
        user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");

        List<RoleRequirements> roleRequirements = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activityStart = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activityEnd = calendar.getTime();
        activity = new ActivityResponseModel(1L, 2L, activityStart, activityEnd, "test",
                Certificate.FOUR_PLUS, roleRequirements, Gender.FEMALE, "TU Delft", Level.Competitive, true);
        appointmentRepo = mock(AppointmentRepository.class);
        user.setRoles(List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)));
        assertFalse(UserEligibilityForActivity.canUserApply(user, activity, Role.PortSideRower, false,
                new ArrayList<>(), appointmentRepo));
    }

    @Test
    public void checkCertificateFalseTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(new RoleInfo(Role.Cox, Level.Competitive));
        List<Availability> availabilities = List.of(new Availability(userStart, userEnd));
        user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");

        List<RoleRequirements> roleRequirements = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activityStart = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activityEnd = calendar.getTime();
        activity = new ActivityResponseModel(1L, 2L, activityStart, activityEnd, "test",
                Certificate.FOUR_PLUS, roleRequirements, Gender.FEMALE, "TU Delft", Level.Competitive, true);
        appointmentRepo = mock(AppointmentRepository.class);
        user.setCertificate(Certificate.C_FOUR);
        assertFalse(UserEligibilityForActivity.canUserApply(user, activity, Role.Cox, false,
                new ArrayList<>(), appointmentRepo));
    }

    @Test
    public void checkGenderOrganizationFalseTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(new RoleInfo(Role.Cox, Level.Competitive));
        List<Availability> availabilities = List.of(new Availability(userStart, userEnd));
        user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");

        List<RoleRequirements> roleRequirements = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activityStart = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activityEnd = calendar.getTime();
        activity = new ActivityResponseModel(1L, 2L, activityStart, activityEnd, "test",
                Certificate.FOUR_PLUS, roleRequirements, Gender.FEMALE, "TU Delft", Level.Competitive, true);
        appointmentRepo = mock(AppointmentRepository.class);
        user.setGender(Gender.MALE);
        assertFalse(UserEligibilityForActivity.canUserApply(user, activity, Role.Cox, false,
                new ArrayList<>(), appointmentRepo));
        user.setGender(Gender.FEMALE);
        user.setOrganization("Dutch Rowing Association");
        assertFalse(UserEligibilityForActivity.canUserApply(user, activity, Role.Cox, false,
                new ArrayList<>(), appointmentRepo));
    }

    @Test
    public void checkSpotAvailabilityFalseTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 2, 11,
                11, 0, 0);
        Date userStart = calendar.getTime();
        calendar.set(2024, 2, 1, 17, 0, 0);
        Date userEnd = calendar.getTime();
        List<RoleInfo> roleInfos = List.of(new RoleInfo(Role.Cox, Level.Competitive));
        List<Availability> availabilities = List.of(new Availability(userStart, userEnd));
        user = new UserDataResponseModel(1L, roleInfos, availabilities, Certificate.FOUR_PLUS,
                Gender.FEMALE, "TU Delft");

        List<RoleRequirements> roleRequirements = List.of(new RoleRequirements(Role.Cox, 1));
        calendar.set(2024, 2, 11,
                13, 0, 0);
        Date activityStart = calendar.getTime();
        calendar.set(2024, 2, 1, 15, 0, 0);
        Date activityEnd = calendar.getTime();
        activity = new ActivityResponseModel(1L, 2L, activityStart, activityEnd, "test",
                Certificate.FOUR_PLUS, roleRequirements, Gender.FEMALE, "TU Delft", Level.Competitive, true);
        appointmentRepo = mock(AppointmentRepository.class);
        when(appointmentRepo.countStatusForActivityRole(1L, Role.Cox, Status.ACCEPTED)).thenReturn(1);
        assertFalse(UserEligibilityForActivity.canUserApply(user, activity, Role.Cox, false,
                new ArrayList<>(), appointmentRepo));
    }

    @Test
    public void checkGenderOrganizationSuccessful() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.FEMALE);

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Laga");

        assertTrue(UserEligibilityForActivity.checkGenderOrganization(activityResponseModel, userResponseModel));
    }

    @Test
    public void checkGenderOrganizationWrongGender() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Laga");

        assertFalse(UserEligibilityForActivity.checkGenderOrganization(activityResponseModel, userResponseModel));
    }

    @Test
    public void checkGenderOrganizationWrongOrganization() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.FEMALE);

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Proteus");

        assertFalse(UserEligibilityForActivity.checkGenderOrganization(activityResponseModel, userResponseModel));

    }

    @Test
    public void checkGenderOrganizationWrongGenderAndOrganization() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);

        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Proteus");

        assertFalse(UserEligibilityForActivity.checkGenderOrganization(activityResponseModel, userResponseModel));
    }

    @Test
    public void checkCertificateCorrectEqual() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.Cox;
        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.Cox, Level.Competitive)), null, Certificate.FOUR_PLUS,
            Gender.FEMALE, "Proteus");

        assertTrue(UserEligibilityForActivity.checkCertificate(role, userResponseModel, activityResponseModel));

    }

    @Test
    public void checkCertificateCorrectHigher() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.Cox;
        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.Cox, Level.Competitive)), null, Certificate.EIGHT_PLUS,
            Gender.FEMALE, "Proteus");

        assertTrue(UserEligibilityForActivity.checkCertificate(role, userResponseModel, activityResponseModel));


    }

    @Test
    public void checkCertificateWrongLower() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.Cox;
        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.Cox, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Proteus");

        assertFalse(UserEligibilityForActivity.checkCertificate(role, userResponseModel, activityResponseModel));


    }

    @Test
    public void checkCertificateLowerButNotCox() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.PortSideRower;
        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.Cox, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Proteus");

        assertTrue(UserEligibilityForActivity.checkCertificate(role, userResponseModel, activityResponseModel));

    }

    @Test
    public void checkRoleSuccessful() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.PortSideRower;

        assertTrue(UserEligibilityForActivity.checkRole(activityResponseModel, role));
    }

    @Test
    public void checkRoleWrong() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.Cox;

        assertFalse(UserEligibilityForActivity.checkRole(activityResponseModel, role));
    }

    @Test
    public void checkLevelNoRole() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.Cox;
        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Proteus");

        assertFalse(UserEligibilityForActivity.checkLevel(activityResponseModel, userResponseModel, role));
    }

    @Test
    public void checkLevelWrongLevel() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.Cox;
        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.Cox, Level.Amateur)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Proteus");

        assertFalse(UserEligibilityForActivity.checkLevel(activityResponseModel, userResponseModel, role));
    }

    @Test
    public void checkLevelCorrectLevel() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(100000, ChronoUnit.DAYS)));
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activityResponseModel.setOrganization("Laga");
        activityResponseModel.setGender(Gender.MALE);
        activityResponseModel.setCertificate(Certificate.FOUR_PLUS);

        Role role = Role.Cox;
        UserDataResponseModel userResponseModel = new UserDataResponseModel(1L,
            List.of(new RoleInfo(Role.Cox, Level.Competitive)), null, Certificate.C_FOUR,
            Gender.FEMALE, "Proteus");

        assertTrue(UserEligibilityForActivity.checkLevel(activityResponseModel, userResponseModel, role));
    }

    @Test
    public void checkTimeActivityStarted() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().minus(100000, ChronoUnit.DAYS)));

        assertFalse(UserEligibilityForActivity.checkTime(activityResponseModel));
    }

    @Test
    public void checkTimeTooLateTraining() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(false);
        activityResponseModel.setLevel(Level.Amateur);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)));

        assertFalse(UserEligibilityForActivity.checkTime(activityResponseModel));
    }

    @Test
    public void checkTimeTooLateCompetition() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)));

        assertFalse(UserEligibilityForActivity.checkTime(activityResponseModel));
    }

    @Test
    public void checkTimeSuccessful() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(26, ChronoUnit.HOURS)));

        assertTrue(UserEligibilityForActivity.checkTime(activityResponseModel));
    }

    @Test
    public void checkSpotAvailabilitySuccessful() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(26, ChronoUnit.HOURS)));

        Role role = Role.Cox;

        AppointmentRepository repository = mock(AppointmentRepository.class);
        when(repository.countStatusForActivityRole(1, role, Status.ACCEPTED)).thenReturn(4);


        assertTrue(UserEligibilityForActivity.checkSpotAvailability(activityResponseModel, role, repository));
    }

    @Test
    public void checkSpotAvailabilityWrong() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(26, ChronoUnit.HOURS)));

        Role role = Role.Cox;

        AppointmentRepository repository = mock(AppointmentRepository.class);
        when(repository.countStatusForActivityRole(1, role, Status.ACCEPTED)).thenReturn(5);


        assertFalse(UserEligibilityForActivity.checkSpotAvailability(activityResponseModel, role, repository));
    }

    @Test
    public void checkSpotAvailabilityWrongTooMany() {
        ActivityResponseModel activityResponseModel = new ActivityResponseModel();
        activityResponseModel.setId(1);
        activityResponseModel.setCompetition(true);
        activityResponseModel.setLevel(Level.Competitive);
        activityResponseModel.setRoles(List.of(new RoleRequirements(Role.Cox, 5)));
        activityResponseModel.setStartingDate(Date.from(Instant.now().plus(26, ChronoUnit.HOURS)));

        Role role = Role.Cox;

        AppointmentRepository repository = mock(AppointmentRepository.class);
        when(repository.countStatusForActivityRole(1, role, Status.ACCEPTED)).thenReturn(7);


        assertFalse(UserEligibilityForActivity.checkSpotAvailability(activityResponseModel, role, repository));
    }

}
