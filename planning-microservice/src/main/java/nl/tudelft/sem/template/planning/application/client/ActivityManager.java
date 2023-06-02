package nl.tudelft.sem.template.planning.application.client;

import java.util.List;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;

public interface ActivityManager {
    ActivityResponseModel retrieveActivity(long id);

    List<ActivityResponseModel> retrieveEligibleActivities(UserDataResponseModel user);

    List<ActivityResponseModel> retrieveOwnedActivities();
}
