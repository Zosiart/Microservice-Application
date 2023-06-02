package nl.tudelft.sem.template.planning.application.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem.template.planning.authentication.AuthManager;
import nl.tudelft.sem.template.planning.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;
import nl.tudelft.sem.template.planning.utils.UserRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserManagerImpl implements UserManager {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthManager authManager;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public UserDataResponseModel retrieveUserData() {
        Long id = Long.parseLong(authManager.getUserId());
        return retrieveUserData(id);
    }

    @Override
    public UserDataResponseModel retrieveUserData(Long id) {
        var response = new UserRequestBuilder(restTemplate, environment.getProperty("service.user"))
            .withRoute("api/user/retrieveUserData")
            .withId(id)
            .get();

        return mapper.convertValue(response.getBody(), new TypeReference<UserDataResponseModel>() {
        });
    }

    @Override
    public MessagingInfoResponseModel retrieveUserMessagingInformation(Long id) {
        var response = new UserRequestBuilder(restTemplate, environment.getProperty("service.user"))
            .withRoute("api/user/messagingInfo")
            .withId(id)
            .withAuth(authManager.getToken())
            .get();

        return mapper.convertValue(response.getBody(), new TypeReference<MessagingInfoResponseModel>() {
        });
    }

}
