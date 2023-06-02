package nl.tudelft.sem.template.planning.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.planning.domain.enums.MessageType;

//DTO for sending messages via the messaging microservice
@Data
@AllArgsConstructor
public class SendMessageRequestModel {
    private String email;
    private String subject;
    private String message;
    private MessageType messageType;
}
