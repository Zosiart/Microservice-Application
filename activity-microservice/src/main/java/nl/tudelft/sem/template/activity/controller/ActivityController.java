package nl.tudelft.sem.template.activity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.activity.authentication.AuthManager;
import nl.tudelft.sem.template.activity.domain.activity.*;
import nl.tudelft.sem.template.activity.dto.ActivityRequest;
import nl.tudelft.sem.template.activity.dto.ActivityResponse;
import nl.tudelft.sem.template.activity.dto.UserResponse;
import nl.tudelft.sem.template.activity.exception.*;
import nl.tudelft.sem.template.activity.service.ActivityService;
import nl.tudelft.sem.template.activity.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/activity")
@SuppressWarnings("PMD")
public class ActivityController {
    private final transient ActivityService service;
    private final transient AuthManager authManager;
    private static final String WRONG_DATE_INPUT = "WRONG_DATE_INPUT";
    private static final String ACTIVITY_NOT_FOUND = "ACTIVITY_NOT_FOUND";
    private static final String ACTIVITY_TYPE_MISMATCH = "ACTIVITY_TYPE_MISMATCH";
    private static final String INVALID_USER = "INVALID_USER";
    private static final String INVALID_ACTIVITY = "INVALID_ACTIVITY";

    @Autowired
    public ActivityController(ActivityService service, AuthManager authManager) {
        this.service = service;
        this.authManager = authManager;
    }

    /**
     * Add a new activity to the database.
     *
     * @param activityRequest activity to be added to the database
     * @return the activity that was added to the database
     */
    @PostMapping(path = {"/create"})
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityRequest activityRequest) {
        try {
            Activity activity = service.createActivity(activityRequest);
            return new ResponseEntity<>(activity, HttpStatus.CREATED);
        } catch (WrongDateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_DATE_INPUT);
        } catch (InvalidActivityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ACTIVITY);
        }
    }


    /**
     * Get all activities in a database.
     * Planner to see all activities
     *
     * @return list of all activities in the database
     */
    @GetMapping(path = {"", "/"})
    public List<ActivityResponse> getAll() {
        return service.getAllActivities();
    }

    /**
     * Gets activity by id.
     *
     * @param id activity id
     * @return activity with the corresponding id
     */
    @GetMapping(path = {"/get/{id}"})
    public ActivityResponse getActivity(@PathVariable Integer id) {
        try {
            ActivityResponse activity = service.getActivity(id);
            return activity;
        } catch (ActivityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ACTIVITY_NOT_FOUND);
        }
    }

    /**
     * Get activities based on filter parameters.
     *
     * @param requestParams - a map containing filter porameters
     * @return a list of activities
     * @throws ParseException when the roles or availabilities failed to be parsed successfully
     */
    @GetMapping("/get")
    public ResponseEntity<Object> handleRequest(@RequestParam Map<String, String> requestParams) throws ParseException {

        var availabilities = Parser.parseAvailabilities(requestParams);
        var roles = Parser.parseRoles(requestParams);

        var mapper = new ObjectMapper();

        var certificate = mapper.convertValue(requestParams.get("certificate"), Certificate.class);
        var gender = mapper.convertValue(requestParams.get("gender"), Gender.class);
        var organization = mapper.convertValue(requestParams.get("organization"), String.class);

        UserResponse userResponseModel = new UserResponse(roles, availabilities, certificate, gender, organization);
        var ownerId = mapper.convertValue(requestParams.get("owner"), Long.class);

        return new ResponseEntity<>(service.getActivities(userResponseModel, ownerId), HttpStatus.OK);
    }

    /**
     * Updates activity information.
     *
     * @param request updated information of the activity
     * @param id      of activity to be edited
     * @return gives HttpStatus. OK if successful
     */
    @PutMapping(path = {"/edit/{id}"})
    public ResponseEntity<Activity> updateActivity(@RequestBody ActivityRequest request, @PathVariable Integer id) {
        try {
            Activity activity = service.updateActivity(request, id);
            return new ResponseEntity<>(activity, HttpStatus.OK);
        } catch (ActivityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ACTIVITY_NOT_FOUND);
        } catch (ActivityTypeMismatchException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ACTIVITY_TYPE_MISMATCH);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_USER);
        } catch (WrongDateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_DATE_INPUT);
        }
    }

    /**
     * Deletes an activity.
     *
     * @param id of activity to be deleted
     */
    @DeleteMapping(path = {"/delete/{id}/user/{userId}"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteActivity(@PathVariable Integer id, @PathVariable Integer userId) {
        try {
            service.deleteActivity(id, userId);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_USER);
        } catch (ActivityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ACTIVITY_NOT_FOUND);
        }
    }

}

