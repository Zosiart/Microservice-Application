package nl.tudelft.sem.template.planning.domain.service;

import java.util.*;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import nl.tudelft.sem.template.planning.application.client.UserManager;
import nl.tudelft.sem.template.planning.authentication.AuthManager;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;
import nl.tudelft.sem.template.planning.domain.entity.UserInfo;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.exception.InsufficientPermissions;
import nl.tudelft.sem.template.planning.exception.ServiceNotAvailable;
import nl.tudelft.sem.template.planning.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanningService {

    private final transient AuthManager authManager;
    private final transient AppointmentRepository appointmentRepository;
    private final transient ActivityManager activityManager;
    private final transient UserManager userManager;

    private final transient ActivityUtils activityUtils;
    private final transient AppointmentUtils appointmentUtils;
    private final transient UserUtils userUtils;

    /**
     * Instantiates a new planning service.
     *
     * @param authManager           - Spring Security component used to authenticate and authorize the user
     * @param appointmentRepository - the repository that handles the appointments
     * @param activityManager       - the activity manager api
     * @param userManager           - the user manager api
     * @param activityUtils         - the activity utility class
     * @param appointmentUtils      - the appointment utility class
     * @param userUtils             - the user utility class
     */
    @Autowired
    public PlanningService(AuthManager authManager, AppointmentRepository appointmentRepository,
                           ActivityManager activityManager,
                           UserManager userManager,
                           ActivityUtils activityUtils,
                           AppointmentUtils appointmentUtils,
                           UserUtils userUtils) {
        this.authManager = authManager;
        this.appointmentRepository = appointmentRepository;
        this.activityManager = activityManager;
        this.userManager = userManager;
        this.activityUtils = activityUtils;
        this.appointmentUtils = appointmentUtils;
        this.userUtils = userUtils;
    }

    /**
     * Apply for an activity.
     *
     * @param activityId - the id of the activity
     * @throws ServiceNotAvailable     - if the activity microservice is not available
     * @throws InsufficientPermissions - if the user is not allowed to apply for the activity
     */
    public void createAppointment(int activityId, Role role) throws InsufficientPermissions, ServiceNotAvailable {
        int userId = Integer.parseInt(authManager.getUserId());

        ActivityResponseModel activity = activityManager.retrieveActivity(activityId);
        UserDataResponseModel user = userManager.retrieveUserData();

        if (!userUtils.canUserApply(user, activity, role, true)) {
            throw new InsufficientPermissions("You are not allowed to join this activity");
        }

        Appointment appointment = new Appointment(activityId, userId, role);
        appointmentRepository.save(appointment);
    }

    /**
     * Changes the status of an appointment.
     *
     * @param appointmentId - the id of the appointment
     * @param status        - the new status of the appointment
     * @return true if the status was changed, false otherwise
     * @throws InsufficientPermissions - if the user is not allowed to change the status
     */
    public boolean resolveAppointment(long appointmentId, Status status)
            throws InsufficientPermissions, ServiceNotAvailable {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();

        if (!(appointmentUtils.validChangeOfStatus(appointment, status)
                && appointmentUtils.appointmentCanBeAccepted(appointment, status))) {
            return false;
        }

        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        if (status == Status.ACCEPTED) {
            cancelOverlappingAppointments(appointment);
        }
        return true;
    }

    /**
     * Cancels all the pending appointments which overlap with the one to which the applicant has just been accepted.
     *
     * @param appointment - the appointment which has been approved.
     */
    private void cancelOverlappingAppointments(Appointment appointment) {
        Map<Long, ActivityResponseModel> pendingActivities
                = activityUtils.getActivitiesWithStatus(appointment.getUserId(), Status.PENDING);
        var activity = activityManager.retrieveActivity(appointment.getActivityId());

        for (var o : pendingActivities.entrySet()) {

            if (activityUtils.checkOverlap(activity, o.getValue())) {
                Optional<Appointment> overlappingAppointment = appointmentRepository.findById(o.getKey());

                if (overlappingAppointment.isPresent()) {
                    Appointment other = overlappingAppointment.get();
                    other.setStatus(Status.DECLINED);
                    appointmentRepository.save(other);
                }
            }
        }
    }

    /**
     * Get all appointments for a user.
     *
     * @return a list of appointments
     */
    public List<AppointmentStatusResponseModel> getAllAppointments() {
        long userId = Integer.parseInt(authManager.getUserId());
        List<Appointment> appointments = appointmentRepository.findAllByUserId(userId);

        return AppointmentStatusResponseModel.fromAppointments(appointments);
    }

    /**
     * Get single appointment details.
     *
     * @param appointmentId - the id of the appointment
     * @return the appointment details
     */
    public AppointmentResponseModel getAppointment(int appointmentId)
        throws ServiceNotAvailable, InsufficientPermissions {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        int userId = Integer.parseInt(authManager.getUserId());
        ActivityResponseModel activity = activityManager.retrieveActivity(appointment.getActivityId());
        UserDataResponseModel user = userManager.retrieveUserData(appointment.getUserId());

        if (activity.getUserId() != userId && appointment.getUserId() != userId) {
            throw new InsufficientPermissions("You are not allowed to view this appointment");
        }

        return new AppointmentResponseModel(UserInfo.fromUserResponseModel(user), appointment);
    }

    /**
     * Get all pending appointments for an activities owned by the user.
     *
     * @return a list of appointments
     */
    public List<PendingAppointmentsResponseModel> getPendingAppointments() throws ServiceNotAvailable {
        List<ActivityResponseModel> activities = activityManager.retrieveOwnedActivities();

        var ids = activities.stream().map(ActivityResponseModel::getId).collect(Collectors.toList());

        List<Appointment> appointments = appointmentRepository.findAllByActivityIdInAndStatus(
            ids,
            Status.PENDING
        );

        return PendingAppointmentsResponseModel.fromAppointments(appointments);
    }

    /**
     * Gets all available eligible activities for this user. The user's id is inferred from the bearer token, then
     * his information is retrieved from the user microservice. With this user data, we poll the activity microservice
     * to find his eligible activities.
     *
     * @return - a list of activity response models
     */
    public List<ActivityResponseModel> getAvailableActivities() {
        // retrieve user info dto from bearer token id
        UserDataResponseModel userData = userManager.retrieveUserData();

        // query the activity microservice with the provided info
        var activities = activityManager.retrieveEligibleActivities(userData);

        var acceptedActivities = activityUtils.getActivitiesWithStatus(userData.getId(),
                Status.ACCEPTED).values();
        return activities.stream().filter(a -> userUtils.canUserApply(userData, a, acceptedActivities))
                .collect(Collectors.toList());
    }

}
