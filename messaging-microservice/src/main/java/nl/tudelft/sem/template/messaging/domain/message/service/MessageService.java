package nl.tudelft.sem.template.messaging.domain.message.service;

import nl.tudelft.sem.template.messaging.domain.message.enums.MessageStatus;
import nl.tudelft.sem.template.messaging.domain.message.exception.InvalidMessageType;
import nl.tudelft.sem.template.messaging.domain.message.factory.MessageFactory;
import nl.tudelft.sem.template.messaging.models.SendMessageRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageFactory messageFactory;

    @Autowired
    public MessageService(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }


    /**
     * Sends a message.
     *
     * @param sendMessageRequestModel - message request model
     * @return - message status
     */
    public MessageStatus sendMessage(SendMessageRequestModel sendMessageRequestModel) throws InvalidMessageType {
        if (sendMessageRequestModel == null || sendMessageRequestModel.getMessageType() == null) {
            throw new InvalidMessageType("Invalid message type");
        }
        return messageFactory.findMessage(sendMessageRequestModel.getMessageType())
            .sendMessage(sendMessageRequestModel) ? MessageStatus.SUCCESS : MessageStatus.FAILED;

    }
}
