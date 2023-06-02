package nl.tudelft.sem.template.activity.service;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.activity.database.ActivityRepository;
import nl.tudelft.sem.template.activity.domain.ActivityGetters;
import nl.tudelft.sem.template.activity.domain.activity.Activity;
import nl.tudelft.sem.template.activity.domain.activity.Competition;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;
import nl.tudelft.sem.template.activity.domain.activity.Training;
import nl.tudelft.sem.template.activity.dto.ActivityRequest;
import nl.tudelft.sem.template.activity.dto.ActivityResponse;
import nl.tudelft.sem.template.activity.dto.UserResponse;
import nl.tudelft.sem.template.activity.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("PMD")
public class ActivityService {

    private final transient ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Create either competition or training activity.
     *
     * @param request ActivityRequest object
     * @return activity created
     */
    public Activity createActivity(ActivityRequest request) throws WrongDateException, InvalidActivityException {
        if (!validActivity(request.getRoles())) {
            throw new InvalidActivityException("Invalid activity");
        }

        //check if starting date is before ending date
        if (request.getStartingDate().before(request.getEndingDate())) {
            if (request.isCompetition()) {
                Competition activity = new Competition(request.getUserId(), request.getStartingDate(),
                    request.getEndingDate(), request.getDescription(), request.getCertificate(),
                    request.getRoles(), request.getGender(), request.getOrganization(),
                    request.getLevel());
                return activityRepository.save(activity);
            } else {
                Training activity = new Training(request.getUserId(), request.getStartingDate(),
                    request.getEndingDate(), request.getDescription(), request.getCertificate(),
                    request.getRoles());
                return activityRepository.save(activity);
            }


        } else {
            throw new WrongDateException("Starting date is after ending date");
        }


    }

    /**
     * Gives all activities in the database.
     *
     * @return list of all activities saved
     */

    public List<ActivityResponse> getAllActivities() {
        return ActivityGetters.getAllActivities(activityRepository);
    }

    /**
     * Gets activity given the id.
     *
     * @param id activity id
     * @return activity with the corresponding id
     */
    public ActivityResponse getActivity(Integer id) throws ActivityNotFoundException {
        return ActivityGetters.getActivity(id, activityRepository);
    }

    /**
     * Updates a specified activity.
     *
     * @param request updated information about an existing activity
     * @param id      of the activity to be edited
     * @return updated Activity
     * @throws ActivityTypeMismatchException when the given request is a different activity type
     */
    public Activity updateActivity(ActivityRequest request, Integer id)
        throws ActivityTypeMismatchException, InvalidUserException, ActivityNotFoundException, WrongDateException {
        Optional<Activity> optionalDbActivity = activityRepository.findById(id);

        if (optionalDbActivity.isEmpty()) {
            throw new ActivityNotFoundException();
        }
        Activity dbActivity = optionalDbActivity.get();
        if (dbActivity instanceof Training && request.isCompetition()) {
            throw new ActivityTypeMismatchException("Activity type mismatch");
        }
        if (dbActivity.getUserId() != request.getUserId()) {
            throw new InvalidUserException("User with id " + request.getUserId() + " does not own activity with id " + id);
        }
        if (request.getEndingDate().before(request.getStartingDate())) {
            throw new WrongDateException("Starting date is after ending date");
        }

        dbActivity.setStartingDate(request.getStartingDate());
        dbActivity.setEndingDate(request.getEndingDate());
        dbActivity.setDescription(request.getDescription());
        dbActivity.setCertificate(request.getCertificate());
        dbActivity.setRoles(request.getRoles());

        if (dbActivity instanceof Competition) {
            ((Competition) dbActivity).setGender(request.getGender());
            ((Competition) dbActivity).setOrganization(request.getOrganization());
            ((Competition) dbActivity).setLevel(request.getLevel());
        }
        return activityRepository.save(dbActivity);
    }


    /**
     * Deletes an activity given the id.
     *
     * @param activityId id of the activity to be deleted
     * @param userId     id of the user who wants to delete the activity
     * @throws InvalidUserException thrown when there does not exist a user with the given id
     */

    public void deleteActivity(Integer activityId, Integer userId) throws InvalidUserException, ActivityNotFoundException {
        if (activityRepository.existsById(activityId) == false) {
            throw new ActivityNotFoundException();
        }
        Activity dbActivity = activityRepository.findById(activityId).get();

        if (dbActivity.getUserId() == userId) {
            activityRepository.deleteById(activityId);
        } else {
            throw new InvalidUserException("User with id " + userId + " does not own activity with id " + activityId);
        }
    }

    /**
     * Get all activities matching the filter parameters.
     * If a filter parameter is null, the filter will be ignored
     *
     * @param userResponseModel - a response model containing a user's availabilities, roles, gender, and certificate
     * @param ownerId           - the id of the user owning the activity
     * @return - a list of activity responses matching the criteria
     */
    public List<ActivityResponse> getActivities(UserResponse userResponseModel, Long ownerId) {
        return ActivityGetters.getActivities(userResponseModel, ownerId, activityRepository);
    }

    /**
     * Checks if activity has any spots left.
     *
     * @param roleAvailabilities list of roles and their availability
     * @return true if there is at least one spot left
     */
    private boolean validActivity(List<RoleAvailability> roleAvailabilities) {
        for (RoleAvailability roleAvailability : roleAvailabilities) {
            if (roleAvailability.getRequiredCount() > 0) {
                return true;
            }
        }
        return false;
    }

}

