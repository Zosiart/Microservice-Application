package nl.tudelft.sem.template.planning.application.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import nl.tudelft.sem.template.planning.authentication.AuthManager;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;
import nl.tudelft.sem.template.planning.utils.ActivityRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ActivityManagerImpl implements ActivityManager {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthManager authManager;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper mapper;

    /**
     * Retrieve an activity with a specified id from the Activity Microservice.
     *
     * @param id - the id of the activity
     * @return the activity
     */
    @Override
    public ActivityResponseModel retrieveActivity(long id) {
        var response = new ActivityRequestBuilder(restTemplate, environment.getProperty("service.activity"))
            .withRoute("api/activity/get")
            .withId(id)
            .get();

        return mapper.convertValue(response.getBody(), new TypeReference<ActivityResponseModel>() {
        });
    }

    /**
     * Retrieve a list of Activities eligible for the user from the Activity Microservice.
     *
     * @param user - the user to fetch activities for
     * @return a list of eligible activities
     */
    @Override
    public List<ActivityResponseModel> retrieveEligibleActivities(UserDataResponseModel user) {
        var response = new ActivityRequestBuilder(restTemplate, environment.getProperty("service.activity"))
            .withRoute("api/activity/get")
            .withRoles(user.getRoles())
            .withCertificate(user.getCertificate())
            .withAvailabilities(user.getAvailabilities())
            .withGender(user.getGender())
            .withOrganization(user.getOrganization())
            .withAuth(authManager.getToken())
            .get();


        return mapper.convertValue(response.getBody(), new TypeReference<List<ActivityResponseModel>>() {
        });
    }

    @Override
    public List<ActivityResponseModel> retrieveOwnedActivities() {
        Long id = Long.parseLong(authManager.getUserId());

        var response = new ActivityRequestBuilder(restTemplate, environment.getProperty("service.activity"))
            .withRoute("api/activity/get")
            .withOwner(id)
            .get();
        return mapper.convertValue(response.getBody(), new TypeReference<List<ActivityResponseModel>>() {
        });
    }

    /*
    @Override
    public ResponseEntity<List<ActivityResponseModel>> retrieveEligibleActivities(String activityServiceAddress,
                                                                                  UserDataResponseModel requestModel,
                                                                                  String bearerToken) {
        String requestUrl = activityServiceAddress + "/api/activity/get/eligible-activities";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);
        HttpEntity<UserDataResponseModel> entity = new HttpEntity<>(requestModel, headers);

        // build request
        ResponseEntity<ActivityResponseModel[]> response = restTemplate.exchange(requestUrl, HttpMethod.POST,
            entity, ActivityResponseModel[].class);

        if (response.getBody() != null) {
            return new ResponseEntity<>(List.of(response.getBody()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
    */
}
