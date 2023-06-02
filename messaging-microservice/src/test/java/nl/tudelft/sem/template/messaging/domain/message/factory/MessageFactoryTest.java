package nl.tudelft.sem.template.messaging.domain.message.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import nl.tudelft.sem.template.messaging.domain.message.entity.Message;
import nl.tudelft.sem.template.messaging.domain.message.entity.messages.Email;
import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;
import nl.tudelft.sem.template.messaging.domain.message.exception.InvalidMessageType;
import org.junit.jupiter.api.Test;

public class MessageFactoryTest {

    @Test
    public void testFindMessageReturnsMessage() throws InvalidMessageType {
        MessageType validMessageType = MessageType.EMAIL;
        Message message = new Email();
        Set<Message> messageSet = new HashSet<>();
        messageSet.add(message);

        MessageFactory factory = new MessageFactory(messageSet);

        Message result = factory.findMessage(validMessageType);
        assertEquals(message, result);
    }

    @Test
    public void testFindMessageThrowsException() {
        // this message type was not registered in the constructor and therefore is invalid
        MessageType invalidMessageType = MessageType.EMAIL;
        Set<Message> messageSet = new HashSet<>();

        MessageFactory factory = new MessageFactory(messageSet);

        assertThrows(InvalidMessageType.class, () -> {
            factory.findMessage(invalidMessageType);
        });
    }
}