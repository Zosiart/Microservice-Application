package nl.tudelft.sem.template.planning.domain.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.entity.RoleRequirements;
import nl.tudelft.sem.template.planning.domain.enums.Level;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {

    private final transient AppointmentRepository appointmentRepository;
    private final transient ActivityUtils activityUtils;
    private final transient Clock clock;

    /**
     * Default Constructor for the user utils.
     *
     * @param appointmentRepository - the appointment repository
     * @param activityUtils - the activity utils
     */
    @Autowired
    public UserUtils(AppointmentRepository appointmentRepository,
                     ActivityUtils activityUtils) {
        this.appointmentRepository = appointmentRepository;
        this.activityUtils = activityUtils;
        this.clock = Clock.systemUTC();
    }

    /**
     * Constructor for testing purposes that allows for a clock to be injected.
     *
     * @param appointmentRepository - the appointment repository
     * @param activityUtils - the activity utils
     * @param clock - the clock
     */
    public UserUtils(AppointmentRepository appointmentRepository,
                     ActivityUtils activityUtils,
                     Clock clock) {
        this.appointmentRepository = appointmentRepository;
        this.activityUtils = activityUtils;
        this.clock = clock;
    }

    /**
     * Checks if the user can apply for a role in an activity.
     *
     * @param user               - the user that is applying
     * @param activity           - the activity the user is applying for
     * @param role               - the role the user is applying for
     * @param acceptedActivities - a collection of activities the user is accepted for
     * @return a boolean representing if a user can apply for the activity
     */
    private boolean canUserApply(UserDataResponseModel user, ActivityResponseModel activity, Role role,
                                 boolean duplicateCheck, Collection<ActivityResponseModel> acceptedActivities) {

        if (duplicateCheck) {

            // Check that user doesn't have appointments for that role
            boolean hasAppointment =
                    appointmentRepository.userHasAppointmentForActivityRole(user.getId(), activity.getId(), role);
            if (hasAppointment) {
                System.out.println("[VALIDATE] user already has appointment for that role");
                return false;
            }
        }

        // Check time
        Instant now = Instant.now(clock);
        Duration timeUntil = Duration.between(now, activity.getStartingDate().toInstant());

        if (timeUntil.isNegative()) {
            System.out.println("[VALIDATE] activity already started/finished");
            return false;
        }

        if (activity.isCompetition() && timeUntil.toHours() < 24
                || timeUntil.toMinutes() < 30) {

            System.out.println("[VALIDATE] it's too late to join");
            return false;
        }

        // Check that user level is >= required level
        Optional<RoleInfo> roleInfo = user.getRoles().stream().filter(x -> x.getRole() == role).findFirst();
        if (roleInfo.isEmpty()) {
            System.out.println("[VALIDATE] user doesn't have the role");
            return false;
        }

        Level userLevel = roleInfo.get().getLevel();
        if (userLevel == Level.Amateur && activity.isCompetition() && activity.getLevel() == Level.Competitive) {
            System.out.println("[VALIDATE] level doesn't match");
            return false;
        }

        // Check accepted < required for role
        Optional<RoleRequirements> requirements = activity.getRoles().stream().filter(x -> x.getRole() == role).findFirst();
        if (requirements.isEmpty()) {
            System.out.println("[VALIDATE] activity doesn't have the role");
            return false;
        }

        // If applies for cox position check if certificate is at least required level
        if (role == Role.Cox && activity.getCertificate().compareTo(user.getCertificate()) > 0) {
            System.out.println("[VALIDATE] higher certificate required");
            return false;
        }

        if (activity.isCompetition()) {

            if (!Objects.equals(activity.getGender(), user.getGender())) {
                System.out.println("[VALIDATE] gender doesn't match for competition");
                return false;
            }

            if (!Objects.equals(activity.getOrganization(), user.getOrganization())) {
                System.out.println("[VALIDATE] organization doesn't match for competition");
                return false;
            }

        }

        int requiredCount = requirements.get().getRequiredCount();
        int acceptedCount = appointmentRepository.countStatusForActivityRole(activity.getId(), role, Status.ACCEPTED);
        if (acceptedCount >= requiredCount) {
            System.out.println("[VALIDATE] not enough spots");
            return false;
        }

        // Check that user doesn't have ACCEPTED activities during that time
        boolean isOverlapping = activityUtils.checkOverlap(activity, acceptedActivities);
        if (isOverlapping) {
            System.out.println("[VALIDATE] user already has an accepted appointment at that time");
            return false;
        }

        return true;
    }

    /**
     * Checks if the user can apply for a role in an activity.
     *
     * @param user     - the user that is applying
     * @param activity - the activity the user is applying for
     * @param role     - the role the user is applying for
     * @return a boolean representing if a user can apply for the activity
     */
    public boolean canUserApply(UserDataResponseModel user, ActivityResponseModel activity, Role role,
                                boolean duplicateCheck) {
        var acceptedActivities = activityUtils.getActivitiesWithStatus(user.getId(), Status.ACCEPTED).values();
        return canUserApply(user, activity, role, duplicateCheck, acceptedActivities);
    }

    /**
     * Checks if the user can apply for any role in an activity.
     *
     * @param user               - the user that is applying
     * @param activity           - the activity the user is applying for
     * @param acceptedActivities - a list of activities the user is accepted for
     * @return a boolean representing if a user can apply for the activity
     */
    public boolean canUserApply(UserDataResponseModel user, ActivityResponseModel activity,
                                Collection<ActivityResponseModel> acceptedActivities) {

        // Check if user already has an accepted appointment for that activity
        boolean isAccepted = appointmentRepository.userHasStatusForActivity(user.getId(), activity.getId(), Status.ACCEPTED);
        if (isAccepted) {
            return false;
        }

        // Check if user can apply for any of their roles
        for (RoleInfo role : user.getRoles()) {
            if (canUserApply(user, activity, role.getRole(), true, acceptedActivities)) {
                return true;
            }
        }

        return false;
    }
}
