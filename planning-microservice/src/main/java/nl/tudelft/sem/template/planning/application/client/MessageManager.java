package nl.tudelft.sem.template.planning.application.client;

import nl.tudelft.sem.template.planning.domain.enums.MessageStatus;
import nl.tudelft.sem.template.planning.models.SendMessageRequestModel;
import org.springframework.http.ResponseEntity;

public interface MessageManager {
    ResponseEntity<MessageStatus> sendMessage(SendMessageRequestModel sendMessageRequestModel);
}
