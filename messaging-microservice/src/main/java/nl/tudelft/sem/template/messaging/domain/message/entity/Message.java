package nl.tudelft.sem.template.messaging.domain.message.entity;

import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;
import nl.tudelft.sem.template.messaging.models.SendMessageRequestModel;

public interface Message {
    boolean sendMessage(SendMessageRequestModel sendMessageRequestModel);

    MessageType getMessageType();
}
