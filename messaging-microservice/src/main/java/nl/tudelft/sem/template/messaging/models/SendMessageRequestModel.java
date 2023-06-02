package nl.tudelft.sem.template.messaging.models;

import lombok.Data;
import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;

@Data
public class SendMessageRequestModel {
    private final String email;
    private final String subject;
    private final String message;
    private final MessageType messageType;
}
