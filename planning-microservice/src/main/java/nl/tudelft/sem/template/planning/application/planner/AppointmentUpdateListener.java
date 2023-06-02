package nl.tudelft.sem.template.planning.application.planner;

import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import nl.tudelft.sem.template.planning.application.client.MessageManager;
import nl.tudelft.sem.template.planning.application.client.UserManager;
import nl.tudelft.sem.template.planning.domain.enums.MessageType;
import nl.tudelft.sem.template.planning.domain.event.UpdateAppointmentStatusEvent;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.planning.models.SendMessageRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This event listener is automatically called when a domain entity is saved
 * which has stored events of type: UpdateAppointmentStatus.
 */
@Component
@Slf4j
public class AppointmentUpdateListener {
    @Autowired
    private transient ActivityManager activityManager;
    @Autowired
    private transient UserManager userManager;
    @Autowired
    private transient MessageManager messageManager;

    /**
     * The name of the function indicated which event is listened to.
     * The format is onEVENT-NAME.
     *
     * @param event The event to react to
     */
    @EventListener
    public void onAppointmentStatusUpdate(UpdateAppointmentStatusEvent event) {
        // retrieve activity details
        ActivityResponseModel activityFound = activityManager.retrieveActivity(event.getActivityId());
        MessagingInfoResponseModel applicantDetails =
            userManager.retrieveUserMessagingInformation(event.getApplicantUserId());

        // send to messaging microservice
        SendMessageRequestModel messageRequestModel =
            new SendMessageRequestModel(applicantDetails.getEmail(), "You have been accepted to an activity!",
                "You have been " + event.getStatus().name() + " from the activity starting on "
                    + activityFound.getStartingDate() + " and ending on " + activityFound.getEndingDate()
                    + " for the position " + event.getApplicantRole(), MessageType.EMAIL);

        messageManager.sendMessage(messageRequestModel);
        log.info("Successfully sent email to {} letting them know that they have been {} to activity with id {}",
            applicantDetails.getEmail(), event.getStatus().name(), event.getActivityId());
    }
}
