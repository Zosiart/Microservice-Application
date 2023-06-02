package nl.tudelft.sem.template.activity.domain;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nl.tudelft.sem.template.activity.database.ActivityRepository;
import nl.tudelft.sem.template.activity.domain.activity.*;
import nl.tudelft.sem.template.activity.dto.ActivityResponse;
import nl.tudelft.sem.template.activity.dto.UserResponse;
import nl.tudelft.sem.template.activity.exception.ActivityNotFoundException;

public class ActivityGetters {

    /**
     * Gets activity given the id.
     *
     * @param id                 activity id
     * @param activityRepository activity repository
     * @return activity with the corresponding id
     */
    public static ActivityResponse getActivity(Integer id, ActivityRepository activityRepository)
        throws ActivityNotFoundException {
        if (activityRepository.existsById(id) == false) {
            throw new ActivityNotFoundException();
        }
        Activity activity = activityRepository.findById(id).get();
        return mapToActivityResponse(activity);
    }

    /**
     * Gives all activities in the database.
     *
     * @param activityRepository activity repository
     * @return list of all activities saved
     */

    public static List<ActivityResponse> getAllActivities(ActivityRepository activityRepository) {
        return activityRepository.findAll().stream().map(x -> mapToActivityResponse(x)).collect(Collectors.toList());
    }

    /**
     * Get all activities matching the filter parameters.
     * If a filter parameter is null, the filter will be ignored
     *
     * @param userResponseModel  - a response model containing a user's availabilities, roles, gender, and certificate
     * @param ownerId            - the id of the user owning the activity
     * @param activityRepository - activity repository
     * @return - a list of activity responses matching the criteria
     */
    public static List<ActivityResponse> getActivities(
        UserResponse userResponseModel, Long ownerId, ActivityRepository activityRepository) {

        if (ownerId != null) {
            return activityRepository.findActivitiesByUserId(ownerId.intValue()).stream()
                .map(ActivityGetters::mapToActivityResponse).collect(
                    Collectors.toList());
        }

        Set<Activity> activitiesSet = new HashSet<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Availability availability : userResponseModel.getAvailabilities()) {
            var foundActivities = activityRepository.findByFilters(ownerId, dateFormat.format(availability.getStartTime()),
                dateFormat.format(availability.getEndTime()), userResponseModel.getGender().ordinal(),
                userResponseModel.getOrganization());
            activitiesSet.addAll(foundActivities);
        }

        var activities = activitiesSet.stream();

        activities = filterRoles(activities, userResponseModel.getRoles(), userResponseModel.getCertificate());

        return activities.map(ActivityGetters::mapToActivityResponse).collect(Collectors.toList());
    }

    private static Stream<Activity> filterRoles(Stream<Activity> activities, List<RoleInfo> roles, Certificate certificate) {
        // Roles
        if (roles != null && !roles.isEmpty()) {
            activities = activities.filter(
                activity -> activity.getRoles().stream().anyMatch(activityRole -> roles.stream().anyMatch(userRole -> {
                    if (activityRole.getRole() != userRole.getRole()) {
                        return false;
                    }

                    // Level (for competitions)
                    if (activity instanceof Competition) {
                        if (((Competition) activity).getLevel() == Level.Competitive
                            && userRole.getLevel() != Level.Competitive) {
                            return false;
                        }
                    }

                    // Certificate
                    if (certificate != null && userRole.getRole() == Role.Cox
                        && activity.getCertificate().compareTo(certificate) > 0) {
                        return false;
                    }

                    return true;
                })));
        }
        return activities;
    }


    /**
     * Maps activity to an ActivityResponse.
     *
     * @param activity to be converted
     * @return ActivityResponse wrapping the activity information
     */
    public static ActivityResponse mapToActivityResponse(Activity activity) {
        if (activity instanceof Competition) {
            return new ActivityResponse(activity.getId(), activity.getUserId(), activity.getStartingDate(),
                activity.getEndingDate(),
                activity.getDescription(), activity.getCertificate(), activity.getRoles(),
                ((Competition) activity).getGender(), ((Competition) activity).getOrganization(),
                ((Competition) activity).getLevel(), true);
        }
        return new ActivityResponse(activity.getId(), activity.getUserId(), activity.getStartingDate(),
            activity.getEndingDate(),
            activity.getDescription(), activity.getCertificate(), activity.getRoles(), false);
    }

}
