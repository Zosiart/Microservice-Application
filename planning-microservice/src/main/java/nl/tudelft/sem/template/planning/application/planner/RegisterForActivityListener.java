package nl.tudelft.sem.template.planning.application.planner;

import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import nl.tudelft.sem.template.planning.application.client.MessageManager;
import nl.tudelft.sem.template.planning.application.client.UserManager;
import nl.tudelft.sem.template.planning.domain.enums.MessageType;
import nl.tudelft.sem.template.planning.domain.event.RegisterForActivityEvent;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.planning.models.SendMessageRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This event listener is automatically called when a domain entity is saved
 * which has stored events of type: RegisterForActivity.
 */
@Component
@Slf4j
public class RegisterForActivityListener {

    private final transient ActivityManager activityManager;
    private final transient UserManager userManager;
    private final transient MessageManager messageManager;

    /**
     * Instantiates a new RegisterForActivityListener.
     *
     * @param activityManager the activity manager
     * @param userManager     the user manager
     * @param messageManager  the message manager
     */
    @Autowired
    public RegisterForActivityListener(ActivityManager activityManager,
                                       UserManager userManager,
                                       MessageManager messageManager) {
        this.activityManager = activityManager;
        this.userManager = userManager;
        this.messageManager = messageManager;
    }


    /**
     * The name of the function indicated which event is listened to.
     * The format is onEVENT-NAME.
     *
     * @param event The event to react to
     */
    @EventListener
    public void onRegistrationForEvent(RegisterForActivityEvent event) {
        // retrieve activity details
        ActivityResponseModel activityFound = activityManager.retrieveActivity(event.getActivityId());

        // retrieve activity owner details
        Long activityOwnerId = activityFound.getUserId();
        MessagingInfoResponseModel activityOwnerDetails = userManager.retrieveUserMessagingInformation(activityOwnerId);
        MessagingInfoResponseModel applicantDetails =
            userManager.retrieveUserMessagingInformation(event.getApplicantUserId());

        // send to messaging microservice
        SendMessageRequestModel messageRequestModel =
            new SendMessageRequestModel(activityOwnerDetails.getEmail(), "Somebody wants to join your activity",
                applicantDetails.getName() + " wants to join your activity for the " + event.getApplicantRole().toString()
                    + " role. Login into the application to accept his request", MessageType.EMAIL);

        messageManager.sendMessage(messageRequestModel);
        log.info("Successfully sent email to {} letting them know that {} wants to join their activity with id {}",
            activityOwnerDetails.getEmail(), applicantDetails.getName(), event.getActivityId());
    }
}
