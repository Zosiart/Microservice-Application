package nl.tudelft.sem.template.messaging.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.template.messaging.domain.message.enums.MessageStatus;
import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;
import nl.tudelft.sem.template.messaging.domain.message.exception.InvalidMessageType;
import nl.tudelft.sem.template.messaging.domain.message.service.MessageService;
import nl.tudelft.sem.template.messaging.models.SendMessageRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MessageControllerTest {

    private MessageService messageService;
    private MessageController messageController;
    String emailAddress = "valid@email.com";
    String subject = "subject";
    String message = "message";

    @BeforeEach
    private void setup() {
        messageService = mock(MessageService.class);
        messageController = new MessageController(messageService);
    }

    @Test
    public void testSendMessageReturnsCreatedStatus() throws InvalidMessageType {
        SendMessageRequestModel requestModel =
            new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        when(messageService.sendMessage(requestModel)).thenReturn(MessageStatus.SUCCESS);
        ResponseEntity<MessageStatus> response = messageController.sendMessage(requestModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testSendMessageReturnsBadRequestStatusOnInvalidMessageType() throws InvalidMessageType {
        SendMessageRequestModel requestModel =
            new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        when(messageService.sendMessage(requestModel)).thenThrow(new InvalidMessageType("Invalid message type."));
        ResponseEntity<MessageStatus> response = messageController.sendMessage(requestModel);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}