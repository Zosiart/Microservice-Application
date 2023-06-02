package nl.tudelft.sem.template.messaging.domain.message.entity.messages;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;
import nl.tudelft.sem.template.messaging.models.SendMessageRequestModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private Email email;


    @Test
    public void testSendMessageInvalidEmail() {
        String emailAddress = "invalidEmail";
        String subject = "subject";
        String message = "message";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertFalse(result);
    }

    @Test
    public void testSendMessageNullMessage() {
        String emailAddress = "valid@email.com";
        String subject = "subject";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, subject, null, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertFalse(result);
    }

    @Test
    public void testSendMessageEmptyMessage() {
        String emailAddress = "invalidEmail";
        String subject = "subject";
        String message = "";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertFalse(result);
    }

    @Test
    public void testSendMessageNullSubject() {
        String emailAddress = "valid@email.com";
        String message = "message";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, null, message, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertFalse(result);
    }

    @Test
    public void testSendMessageEmptySubject() {
        String emailAddress = "valid@email.com";
        String subject = "";
        String message = "message";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, null, message, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertFalse(result);
    }

    @Test
    public void testSendMessageValidInput() {
        String emailAddress = "valid@email.com";
        String subject = "subject";
        String message = "message";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertTrue(result);
    }

    @Test
    public void nullEmailsInvalid() {
        String emailAddress = null;
        String subject = "Important!";
        String message = "Lorem Ipsum";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertFalse(result);
    }

    @Test
    public void correctEmailFields() {
        String emailAddress = "valid@email.com";
        String subject = "So Important";
        String message = "Lorem Ipsum";
        SendMessageRequestModel request = new SendMessageRequestModel(emailAddress, subject, message, MessageType.EMAIL);

        boolean result = email.sendMessage(request);
        assertTrue(result);
        ArgumentCaptor<SimpleMailMessage> sentMessage = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(sentMessage.capture());

        SimpleMailMessage messageValue = sentMessage.getValue();
        assertEquals("sem30a@onet.pl", messageValue.getFrom());
        assertEquals("valid@email.com", messageValue.getTo()[0]);
        assertEquals(1, messageValue.getTo().length);
        assertEquals("So Important", messageValue.getSubject());
        assertEquals("Lorem Ipsum", messageValue.getText());

    }
}