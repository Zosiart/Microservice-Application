package nl.tudelft.sem.template.planning.domain.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import nl.tudelft.sem.template.planning.authentication.AuthManager;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import org.springframework.stereotype.Service;

@Service
public class ActivityUtils {

    private final transient AuthManager authManager;
    private final transient AppointmentRepository appointmentRepository;
    private final transient ActivityManager activityManager;

    /**
     * Constructor for ActivityUtils.
     *
     * @param authManager - the authentication manager
     * @param appointmentRepository - the repository of appointments
     * @param activityManager - the activity manager
     */
    public ActivityUtils(AuthManager authManager,
                         AppointmentRepository appointmentRepository,
                         ActivityManager activityManager) {
        this.authManager = authManager;
        this.appointmentRepository = appointmentRepository;
        this.activityManager = activityManager;
    }

    /**
     * Retrieves all the activities the user has a status for.
     *
     * @param userId - the id of the user
     * @param status - the status to look for
     * @return a map of appointmentId->activity
     */
    public Map<Long, ActivityResponseModel> getActivitiesWithStatus(long userId, Status status) {
        var appointments = appointmentRepository.findAllByUserIdAndStatus(userId, status);

        var activities = new HashMap<Long, ActivityResponseModel>();
        for (var app : appointments) {
            activities.put(app.getId(), activityManager.retrieveActivity(app.getActivityId()));
        }

        return activities;
    }

    /**
     * Check if the time of an activity overlaps the other activity.
     *
     * @param activity - the activity to test
     * @param other    - the other activity which the 'activity' will be tested against
     * @return a boolean representing if the activity overlaps the other activity
     */
    public static boolean checkOverlap(ActivityResponseModel activity, ActivityResponseModel other) {
        return activity.getEndingDate().after(other.getStartingDate())
                && activity.getStartingDate().before(other.getEndingDate());
    }

    /**
     * Check if the time of an activity overlaps the other activities.
     *
     * @param activity - the activity to test
     * @param other    - the other activities which the 'activity' will be tested against
     * @return a boolean representing if the activity overlaps any of the other activities
     */
    public static boolean checkOverlap(ActivityResponseModel activity, Collection<ActivityResponseModel> other) {

        for (var o : other) {
            if (checkOverlap(activity, o)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Verify that the user has the required role to resolve the appointment.
     *
     * @param activityId - the id of the activity
     * @return a boolean representing if the user is allowed to change the status
     */
    public boolean verifyIfUserIsAllowedToChangeStatus(long activityId) {
        long userId = Long.parseLong(authManager.getUserId());

        try {
            ActivityResponseModel activity = this.activityManager.retrieveActivity(activityId);
            return activity != null && activity.getUserId() == userId;
        } catch (Exception e) {
            return false;
        }
    }
}
