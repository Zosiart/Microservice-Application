package nl.tudelft.sem.template.planning.application.client;

import nl.tudelft.sem.template.planning.models.MessagingInfoResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;

public interface UserManager {

    UserDataResponseModel retrieveUserData();

    UserDataResponseModel retrieveUserData(Long id);

    MessagingInfoResponseModel retrieveUserMessagingInformation(Long id);
}
