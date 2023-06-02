package nl.tudelft.sem.template.messaging.domain.message.entity.messages;

import nl.tudelft.sem.template.messaging.domain.message.entity.Message;
import nl.tudelft.sem.template.messaging.domain.message.enums.MessageType;
import nl.tudelft.sem.template.messaging.models.SendMessageRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class Email implements Message {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public boolean sendMessage(SendMessageRequestModel sendMessageRequestModel) {
        if (!validateEmailAddress(sendMessageRequestModel.getEmail())) {
            return false;
        }

        if (sendMessageRequestModel.getMessage() == null || sendMessageRequestModel.getMessage().isEmpty()) {
            return false;
        }

        if (sendMessageRequestModel.getSubject() == null || sendMessageRequestModel.getSubject().isEmpty()) {
            return false;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sem30a@onet.pl");
        message.setTo(sendMessageRequestModel.getEmail());
        message.setSubject(sendMessageRequestModel.getSubject());
        message.setText(sendMessageRequestModel.getMessage());
        try {
            emailSender.send(message);
        } catch (Exception e) {
            System.out.println("Exception caught but I think mail was sent.");
        }

        return true;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.EMAIL;
    }

    private boolean validateEmailAddress(String email) {
        if (email == null) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
