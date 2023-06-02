package nl.tudelft.sem.template.messaging.domain.message.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nl.tudelft.sem.template.messaging.domain.message.entity.Message;
import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;
import nl.tudelft.sem.template.messaging.domain.message.exception.InvalidMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {
    private Map<MessageType, Message> messageTypes;

    @Autowired
    public MessageFactory(Set<Message> messageSet) {
        constructMessages(messageSet);
    }

    /**
     * Factory method for fetching the correct message type.
     *
     * @param messageType - the type of message to be sent
     * @return the correct message type
     * @throws InvalidMessageType - if the message type is not supported
     */
    public Message findMessage(MessageType messageType) throws InvalidMessageType {
        if (messageTypes.containsKey(messageType)) {
            return messageTypes.get(messageType);
        } else {
            throw new InvalidMessageType("Invalid message type");
        }
    }

    /**
     * Constructs a map of message types.
     *
     * @param messageSet - the set of messages to be sent
     */
    private void constructMessages(Set<Message> messageSet) {
        messageTypes = new HashMap<>();
        messageSet.forEach(message -> messageTypes.put(message.getMessageType(), message));
    }
}
