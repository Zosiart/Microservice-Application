package nl.tudelft.sem.template.planning.application.client;

import nl.tudelft.sem.template.planning.domain.enums.MessageStatus;
import nl.tudelft.sem.template.planning.models.SendMessageRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageManagerImpl implements MessageManager {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @Override
    public ResponseEntity<MessageStatus> sendMessage(SendMessageRequestModel sendMessageRequestModel) {

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(sendMessageRequestModel, headers);
        String messageServiceAddress = environment.getProperty("service.message");
        String requestUrl = messageServiceAddress + "/send";
        return restTemplate.exchange(requestUrl, HttpMethod.POST, entity, MessageStatus.class);
    }
}
