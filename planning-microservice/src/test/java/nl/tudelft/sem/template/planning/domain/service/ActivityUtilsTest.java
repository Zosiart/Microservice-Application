package nl.tudelft.sem.template.planning.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import nl.tudelft.sem.template.planning.authentication.AuthManager;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActivityUtilsTest {

    private AuthManager authManager;
    private AppointmentRepository appointmentRepository;
    private ActivityManager activityManager;
    private ActivityUtils activityUtils;

    /**
     * Sets up the mocks before running tests.
     */
    @BeforeEach
    public void setup() {
        authManager = mock(AuthManager.class);
        appointmentRepository = mock(AppointmentRepository.class);
        activityManager = mock(ActivityManager.class);

        activityUtils = new ActivityUtils(authManager, appointmentRepository, activityManager);
    }

    @Test
    void testGetActivitiesWithStatus_All() {
        // Given
        when(authManager.getUserId()).thenReturn("1");
        var appointment1 = new Appointment(1, 1, Role.PortSideRower);
        appointment1.setId(1);
        appointment1.setStatus(Status.ACCEPTED);
        var appointment2 = new Appointment(2, 1, Role.ScullingRower);
        appointment2.setId(2);
        appointment2.setStatus(Status.ACCEPTED);
        var appointments = Arrays.asList(appointment1, appointment2);
        when(appointmentRepository.findAllByUserIdAndStatus(1, Status.ACCEPTED)).thenReturn(appointments);
        var activity1 =
            new ActivityResponseModel(1, 1, new Date(), new Date(), "Test Activity 1", null, null, null, null, null, false);
        var activity2 =
            new ActivityResponseModel(2, 1, new Date(), new Date(), "Test Activity 2", null, null, null, null, null, false);
        when(activityManager.retrieveActivity(1)).thenReturn(activity1);
        when(activityManager.retrieveActivity(2)).thenReturn(activity2);

        Map<Long, ActivityResponseModel> result = activityUtils.getActivitiesWithStatus(1, Status.ACCEPTED);

        assertEquals(2, result.size());
        assertEquals(activity1, result.get(1L));
        assertEquals(activity2, result.get(2L));
    }

    @Test
    void testCheckOverlap_True() {
        var activity =
            new ActivityResponseModel(1, 1, new Date(), new Date(), "Test Activity 1", null, null, null, null, null, false);
        var other =
            new ActivityResponseModel(2, 1, new Date(), new Date(), "Test Activity 2", null, null, null, null, null, false);
        activity.setStartingDate(new Date(2000, 1, 1));
        activity.setEndingDate(new Date(2000, 1, 2));
        other.setStartingDate(new Date(2000, 1, 1));
        other.setEndingDate(new Date(2000, 1, 3));

        boolean result = ActivityUtils.checkOverlap(activity, other);

        assertTrue(result);
    }

    @Test
    void testCheckOverlap_False() {
        var activity =
            new ActivityResponseModel(1, 1, new Date(), new Date(), "Test Activity 1", null, null, null, null, null, false);
        var other =
            new ActivityResponseModel(2, 1, new Date(), new Date(), "Test Activity 2", null, null, null, null, null, false);
        activity.setStartingDate(new Date(2000, 1, 1));
        activity.setEndingDate(new Date(2000, 1, 2));
        other.setStartingDate(new Date(2000, 1, 3));
        other.setEndingDate(new Date(2000, 1, 4));

        boolean result = ActivityUtils.checkOverlap(activity, other);

        assertFalse(result);
    }

    @Test
    void testCheckOverlapWithCollection_True() {
        var activity =
            new ActivityResponseModel(1, 1, new Date(), new Date(), "Test Activity 1", null, null, null, null, null, false);
        activity.setStartingDate(new Date(2000, 1, 1));
        activity.setEndingDate(new Date(2000, 1, 2));

        var other1 =
            new ActivityResponseModel(2, 1, new Date(), new Date(), "Test Activity 2", null, null, null, null, null, false);
        other1.setStartingDate(new Date(2000, 1, 1));
        other1.setEndingDate(new Date(2000, 1, 3));

        var other2 =
            new ActivityResponseModel(3, 1, new Date(), new Date(), "Test Activity 3", null, null, null, null, null, false);
        other2.setStartingDate(new Date(2000, 1, 3));
        other2.setEndingDate(new Date(2000, 1, 4));

        boolean result = ActivityUtils.checkOverlap(activity, Arrays.asList(other1, other2));

        assertTrue(result);
    }

    @Test
    void testCheckOverlapWithCollection_False() {
        var activity =
            new ActivityResponseModel(1, 1, new Date(), new Date(), "Test Activity 1", null, null, null, null, null, false);
        activity.setStartingDate(new Date(2000, 1, 1));
        activity.setEndingDate(new Date(2000, 1, 2));

        var other1 =
            new ActivityResponseModel(2, 1, new Date(), new Date(), "Test Activity 2", null, null, null, null, null, false);
        other1.setStartingDate(new Date(2000, 1, 3));
        other1.setEndingDate(new Date(2000, 1, 4));

        var other2 =
            new ActivityResponseModel(3, 1, new Date(), new Date(), "Test Activity 3", null, null, null, null, null, false);
        other2.setStartingDate(new Date(2000, 1, 5));
        other2.setEndingDate(new Date(2000, 1, 6));

        boolean result = ActivityUtils.checkOverlap(activity, Arrays.asList(other1, other2));

        assertFalse(result);
    }

    @Test
    void testVerifyIfUserIsAllowedToChangeStatus_True() {
        when(authManager.getUserId()).thenReturn("1");
        var activity =
            new ActivityResponseModel(1, 1, new Date(), new Date(), "Test Activity 1", null, null, null, null, null, false);
        when(activityManager.retrieveActivity(1)).thenReturn(activity);

        boolean result = activityUtils.verifyIfUserIsAllowedToChangeStatus(1);

        assertTrue(result);
    }

    @Test
    void testVerifyIfUserIsAllowedToChangeStatus_False() {
        when(authManager.getUserId()).thenReturn("2");
        var activity =
            new ActivityResponseModel(1, 1, new Date(), new Date(), "Test Activity 1", null, null, null, null, null, false);
        when(activityManager.retrieveActivity(1)).thenReturn(activity);

        boolean result = activityUtils.verifyIfUserIsAllowedToChangeStatus(1);

        assertFalse(result);
    }

    @Test
    void testVerifyIfUserIsAllowedToChangeStatus_CatchBlock() {
        when(authManager.getUserId()).thenReturn("2");
        when(activityManager.retrieveActivity(1)).thenThrow(RuntimeException.class);

        boolean result = activityUtils.verifyIfUserIsAllowedToChangeStatus(1);

        assertFalse(result);
    }

}
