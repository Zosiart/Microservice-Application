package nl.tudelft.sem.template.messaging.domain.message.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.template.messaging.domain.message.entity.Message;
import nl.tudelft.sem.template.messaging.domain.message.enums.MessageStatus;
import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;
import nl.tudelft.sem.template.messaging.domain.message.exception.InvalidMessageType;
import nl.tudelft.sem.template.messaging.domain.message.factory.MessageFactory;
import nl.tudelft.sem.template.messaging.models.SendMessageRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageServiceTest {

    String emailAddress = "valid@email.com";
    String subject = "subject";
    String message = "message";

    private Message mockMessage;
    private MessageFactory mockFactory;

    @BeforeEach
    private void setup() {
        mockMessage = mock(Message.class);
        mockFactory = mock(MessageFactory.class);
    }

    @Test
    public void testSendMessageSuccess() throws InvalidMessageType {
        SendMessageRequestModel requestModel =
            new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        when(mockMessage.sendMessage(requestModel)).thenReturn(true);
        when(mockFactory.findMessage(requestModel.getMessageType())).thenReturn(mockMessage);
        MessageService service = new MessageService(mockFactory);

        assertEquals(MessageStatus.SUCCESS, service.sendMessage(requestModel));
    }

    @Test
    public void testSendMessageFailed() throws InvalidMessageType {
        SendMessageRequestModel requestModel =
            new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        when(mockMessage.sendMessage(requestModel)).thenReturn(false);
        when(mockFactory.findMessage(requestModel.getMessageType())).thenReturn(mockMessage);
        MessageService service = new MessageService(mockFactory);

        assertEquals(MessageStatus.FAILED, service.sendMessage(requestModel));
    }

    @Test
    public void testSendMessageInvalidMessageType() throws InvalidMessageType {
        SendMessageRequestModel requestModel =
            new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        when(mockFactory.findMessage(requestModel.getMessageType())).thenThrow(
            new InvalidMessageType("Invalid message type"));
        MessageService service = new MessageService(mockFactory);

        assertThrows(InvalidMessageType.class, () -> {
            service.sendMessage(requestModel);
        });
    }

    @Test
    public void testSendMessageNullRequest() {
        MessageService service = new MessageService(mockFactory);
        assertThrows(InvalidMessageType.class, () -> {
            service.sendMessage(null);
        });
    }

    @Test
    public void testSendMessageMessageTypeNull() {
        SendMessageRequestModel requestModel =
            new SendMessageRequestModel(emailAddress, subject, message, null);

        MessageService service = new MessageService(mockFactory);
        assertThrows(InvalidMessageType.class, () -> {
            service.sendMessage(requestModel);
        });
    }
}