package nl.tudelft.sem.template.planning.domain.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.entity.RoleRequirements;
import nl.tudelft.sem.template.planning.domain.enums.*;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserUtilsTest {

    private AppointmentRepository appointmentRepository;
    private UserUtils userUtils;

    /**
     * Setup for the tests.
     */
    @BeforeEach
    public void setup() {
        appointmentRepository = mock(AppointmentRepository.class);
        ActivityUtils activityUtils = new ActivityUtils(null, appointmentRepository, null);
        String instantExpected = "2014-12-22T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        userUtils = new UserUtils(appointmentRepository, activityUtils, clock);
    }

    @Test
    public void activityBeingCompetitionStartsInLessThan24Hours() {
        when(appointmentRepository.findAllByUserIdAndStatus(1L, Status.ACCEPTED)).thenReturn(new ArrayList<>());
        when(appointmentRepository.countStatusForActivityRole(1L, Role.PortSideRower, Status.ACCEPTED))
                .thenReturn(0);

        UserDataResponseModel user = new UserDataResponseModel();
        user.setId(1L);
        user.setRoles(List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)));
        user.setGender(Gender.MALE);
        user.setOrganization("TU Delft");

        ActivityResponseModel activity = new ActivityResponseModel();
        activity.setId(1L);
        activity.setStartingDate(Date.from(Instant.now(Clock.fixed(Instant.parse("2014-12-23T10:17:30Z"),
                ZoneId.of("UTC")))));
        activity.setCompetition(true);
        activity.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activity.setGender(Gender.MALE);
        activity.setOrganization("TU Delft");

        assertTrue(userUtils.canUserApply(user, activity, Role.PortSideRower, false));
    }

    @Test
    public void activityBeingTrainingStartsInLessThan30min() {
        when(appointmentRepository.findAllByUserIdAndStatus(1L, Status.ACCEPTED)).thenReturn(new ArrayList<>());
        when(appointmentRepository.countStatusForActivityRole(1L, Role.PortSideRower, Status.ACCEPTED))
                .thenReturn(0);

        UserDataResponseModel user = new UserDataResponseModel();
        user.setId(1L);
        user.setRoles(List.of(new RoleInfo(Role.PortSideRower, Level.Competitive)));
        user.setGender(Gender.MALE);
        user.setOrganization("TU Delft");

        ActivityResponseModel activity = new ActivityResponseModel();
        activity.setId(1L);
        activity.setStartingDate(Date.from(Instant.now(Clock.fixed(Instant.parse("2014-12-22T10:45:35Z"),
                ZoneId.of("UTC")))));
        activity.setCompetition(false);
        activity.setRoles(List.of(new RoleRequirements(Role.PortSideRower, 1)));
        activity.setGender(Gender.MALE);
        activity.setOrganization("TU Delft");

        assertTrue(userUtils.canUserApply(user, activity, Role.PortSideRower, false));
    }

    @Test
    public void certificateIsEqualToRequired() {
        when(appointmentRepository.findAllByUserIdAndStatus(1L, Status.ACCEPTED)).thenReturn(new ArrayList<>());
        when(appointmentRepository.countStatusForActivityRole(1L, Role.Cox, Status.ACCEPTED))
                .thenReturn(0);

        UserDataResponseModel user = new UserDataResponseModel();
        user.setId(1L);
        user.setRoles(List.of(new RoleInfo(Role.Cox, Level.Competitive)));
        user.setGender(Gender.MALE);
        user.setOrganization("TU Delft");
        user.setCertificate(Certificate.C_FOUR);

        ActivityResponseModel activity = new ActivityResponseModel();
        activity.setId(1L);
        activity.setStartingDate(Date.from(Instant.now(Clock.fixed(Instant.parse("2014-12-23T10:45:35Z"),
                ZoneId.of("UTC")))));
        activity.setCompetition(true);
        activity.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activity.setGender(Gender.MALE);
        activity.setOrganization("TU Delft");
        activity.setCertificate(Certificate.C_FOUR);

        assertTrue(userUtils.canUserApply(user, activity, Role.Cox, false));
    }

    @Test
    public void requiredCountIsSmallerNotEqualToOffered() {
        when(appointmentRepository.findAllByUserIdAndStatus(1L, Status.ACCEPTED)).thenReturn(new ArrayList<>());
        when(appointmentRepository.countStatusForActivityRole(1L, Role.Cox, Status.ACCEPTED))
                .thenReturn(0);

        UserDataResponseModel user = new UserDataResponseModel();
        user.setId(1L);
        user.setRoles(List.of(new RoleInfo(Role.Cox, Level.Competitive)));
        user.setGender(Gender.MALE);
        user.setOrganization("TU Delft");
        user.setCertificate(Certificate.EIGHT_PLUS);

        ActivityResponseModel activity = new ActivityResponseModel();
        activity.setId(1L);
        activity.setStartingDate(Date.from(Instant.now(Clock.fixed(Instant.parse("2014-12-23T10:45:35Z"),
                ZoneId.of("UTC")))));
        activity.setCompetition(true);
        activity.setRoles(List.of(new RoleRequirements(Role.Cox, 1)));
        activity.setGender(Gender.MALE);
        activity.setOrganization("TU Delft");
        activity.setCertificate(Certificate.C_FOUR);

        assertTrue(userUtils.canUserApply(user, activity, Role.Cox, false));
    }
}
