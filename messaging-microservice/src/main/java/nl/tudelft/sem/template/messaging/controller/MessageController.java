package nl.tudelft.sem.template.messaging.controller;

import nl.tudelft.sem.template.messaging.domain.message.enums.MessageStatus;
import nl.tudelft.sem.template.messaging.domain.message.exception.InvalidMessageType;
import nl.tudelft.sem.template.messaging.domain.message.service.MessageService;
import nl.tudelft.sem.template.messaging.models.SendMessageRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class MessageController {

    private MessageService messageService;

    /**
     * Instantiates an MessageController.
     *
     * @param messageService - messaging service
     */
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    /**
     * Sends a message.
     *
     * @param sendMessageRequestModel - message request model
     * @return - response entity containing message status
     */
    @PostMapping("/send")
    public ResponseEntity<MessageStatus> sendMessage(@RequestBody SendMessageRequestModel sendMessageRequestModel) {
        try {
            return new ResponseEntity(messageService.sendMessage(sendMessageRequestModel), HttpStatus.CREATED);
        } catch (InvalidMessageType e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
