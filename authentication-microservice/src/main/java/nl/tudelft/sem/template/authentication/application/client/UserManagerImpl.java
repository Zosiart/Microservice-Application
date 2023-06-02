package nl.tudelft.sem.template.authentication.application.client;

import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import nl.tudelft.sem.template.authentication.models.RegistrationUserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<Long> registerUser(String userServiceAddress, RegistrationRequestModel registerUserRequestModel) {
        String requestUrl = userServiceAddress + "/api/user/register";
        RegistrationUserRequestModel dtoRequest =
            new RegistrationUserRequestModel(registerUserRequestModel.getUsername(), registerUserRequestModel.getGender(),
                registerUserRequestModel.getEmail(), registerUserRequestModel.getRoleInfoList(),
                registerUserRequestModel.getAvailabilities(), registerUserRequestModel.getCertificate(),
                registerUserRequestModel.getOrganization());

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // build request
        HttpEntity<RegistrationUserRequestModel> entity = new HttpEntity<>(dtoRequest, headers);

        return restTemplate.postForEntity(requestUrl, entity, Long.class);
    }
}
