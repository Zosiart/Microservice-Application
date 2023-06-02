package nl.tudelft.sem.template.planning.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.entity.RoleRequirements;
import nl.tudelft.sem.template.planning.domain.enums.Level;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.repository.AppointmentRepository;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;

public class UserEligibilityForActivity {
    /**
     * Checks if the user can apply for a role in an activity.
     *
     * @param user               - the user that is applying
     * @param activity           - the activity the user is applying for
     * @param role               - the role the user is applying for
     * @param acceptedActivities - a collection of activities the user is accepted for
     * @param appointmentRepository - the repository of appointments
     * @return a boolean representing if a user can apply for the activity
     */
    public static boolean canUserApply(UserDataResponseModel user, ActivityResponseModel activity, Role role,
                                       boolean duplicateCheck, Collection<ActivityResponseModel> acceptedActivities,
                                       AppointmentRepository appointmentRepository) {

        if ((duplicateCheck
            && appointmentRepository.userHasAppointmentForActivityRole(user.getId(), activity.getId(), role))
            || !checkTime(activity) || !checkLevel(activity, user, role) || !checkRole(activity, role)
            || !checkCertificate(role, user, activity) || !checkGenderOrganization(activity, user)
            || !checkSpotAvailability(activity, role, appointmentRepository)
            || ActivityUtils.checkOverlap(activity, acceptedActivities)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the user applies for the activity in time.
     *
     * @param activity - the activity the user is applying for
     * @return a boolean representing if the user applies for the activity in time
     */
    public static boolean checkTime(ActivityResponseModel activity) {
        Instant now = Instant.now();
        Duration timeUntil = Duration.between(now, activity.getStartingDate().toInstant());

        if (timeUntil.isNegative()) {
            System.out.println("[VALIDATE] activity already started/finished");
            return false;
        }

        if ((activity.isCompetition() && timeUntil.toHours() < 24) || timeUntil.toMinutes() < 30) {

            System.out.println("[VALIDATE] it's too late to join");
            return false;
        }

        return true;
    }

    /**
     * Checks if the user has the required level to apply for the activity.
     *
     * @param activity - the activity the user is applying for
     * @param user - the user that is applying
     * @param role - the role the user is applying for
     * @return a boolean representing if the user has the required level
     */
    public static boolean checkLevel(ActivityResponseModel activity, UserDataResponseModel user, Role role) {
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


        return true;
    }

    /**
     * Checks if the activity has the required role.
     *
     * @param activity - the activity the user is applying for
     * @param role     - the role the user is applying for
     * @return a boolean representing if the activity has the required role
     */
    public static boolean checkRole(ActivityResponseModel activity, Role role) {
        Optional<RoleRequirements> requirements = activity.getRoles().stream()
            .filter(x -> x.getRole() == role).findFirst();
        if (requirements.isEmpty()) {
            System.out.println("[VALIDATE] activity doesn't have the role");
            return false;
        }

        return true;
    }

    /**
     * Checks whether the user has the required certificate for the activity, if he/she applies for the cox role.
     *
     * @param role the role the user applies for
     * @param user the user that applies
     * @param activity the activity the user applies for
     * @return true if the user has the certificate and applies for cox role, false otherwise
     */
    public static boolean checkCertificate(Role role, UserDataResponseModel user, ActivityResponseModel activity) {
        if (role == Role.Cox && activity.getCertificate().compareTo(user.getCertificate()) > 0) {
            System.out.println("[VALIDATE] higher certificate required");
            return false;
        }

        return true;
    }

    /**
     * If activity is a competition, it checks whether user gender and organization match activity requirements.
     *
     * @param activity - the activity the user is applying for
     * @param user - the user that is applying
     * @return a boolean representing whether gender and organization match
     */
    public static boolean checkGenderOrganization(ActivityResponseModel activity, UserDataResponseModel user) {
        if (activity.isCompetition()) {
            return (activity.getGender() == user.getGender() && activity.getOrganization()
                .equals(user.getOrganization()));
        }
        return true;
    }

    /**
     * Checks if the activity has a spot available for the role.
     *
     *
     * @param activity - the activity to check
     * @param role - the role to check
     * @param appointmentRepository - the repository to check the database
     * @return a boolean representing if the activity has a spot available for the role
     */
    public static boolean checkSpotAvailability(ActivityResponseModel activity, Role role,
                                                AppointmentRepository appointmentRepository) {
        int requiredCount = activity.getRoles().stream().filter(x -> x.getRole() == role).findFirst().get()
            .getRequiredCount();
        int currentCount = appointmentRepository.countStatusForActivityRole(activity.getId(), role, Status.ACCEPTED);
        if (currentCount >= requiredCount) {
            System.out.println("[VALIDATE] not enough spots");
            return false;
        }

        return true;
    }


}
