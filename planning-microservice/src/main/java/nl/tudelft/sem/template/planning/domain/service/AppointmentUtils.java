package nl.tudelft.sem.template.planning.domain.service;

import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import nl.tudelft.sem.template.planning.application.client.UserManager;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.exception.InsufficientPermissions;
import org.springframework.stereotype.Service;

@Service
public class AppointmentUtils {

    private final transient ActivityManager activityManager;
    private final transient UserManager userManager;
    private final transient ActivityUtils activityUtils;
    private final transient UserUtils userUtils;


    /**
     * Instantiates a new appointment utils.
     *
     * @param activityManager - the activity manager api
     * @param userManager - the user manager api
     * @param activityUtils - the activity utility class
     * @param userUtils - the user utility class
     */
    public AppointmentUtils(ActivityManager activityManager,
                            UserManager userManager,
                            ActivityUtils activityUtils,
                            UserUtils userUtils) {
        this.activityManager = activityManager;
        this.userManager = userManager;
        this.activityUtils = activityUtils;
        this.userUtils = userUtils;
    }

    /**
     * A valid change of status is one in which:
     * The appointment is not being updated to PENDING (it can only go from PENDING to ACCEPTED or DECLINED)
     * The appointment was PENDING before the update;
     * The party who is updating the appointment is authorised to do so.
     * This method checks these three conditions.
     *
     * @param appointment - the appointment being checked
     * @param status - the status being checked
     * @return true iff the appointment checks the previously described conditions.
     * @throws InsufficientPermissions if the requesting party is not allowed to change the status.
     */
    public boolean validChangeOfStatus(Appointment appointment, Status status) throws InsufficientPermissions {
        if (status == Status.PENDING || appointment.getStatus() != Status.PENDING) {
            return false;
        }
        if (!activityUtils.verifyIfUserIsAllowedToChangeStatus(appointment.getActivityId())) {
            throw new InsufficientPermissions("You are not allowed to change the status of this appointment");
        }
        return true;
    }

    /**
     * For accepted appointments, checks that the applicant is still eligible for the activity. (It would
     * otherwise be possible for users that have changed their roles / availabilities to be accepted to activities that
     * they are no longer eligible for).
     *
     * @param appointment - the appointment being checked
     * @param status - the status being checked
     * @return true if the appointment is not accepted or if the user is eligible for it.
     * @throws InsufficientPermissions otherwise
     */
    public boolean appointmentCanBeAccepted(Appointment appointment, Status status) throws InsufficientPermissions {
        var user = userManager.retrieveUserData(appointment.getUserId());
        var activity = activityManager.retrieveActivity(appointment.getActivityId());

        if (status == Status.ACCEPTED && !userUtils.canUserApply(user, activity, appointment.getRole(), false)) {
            throw new InsufficientPermissions("The user can no longer join this appointment");
        }
        return true;
    }
}
