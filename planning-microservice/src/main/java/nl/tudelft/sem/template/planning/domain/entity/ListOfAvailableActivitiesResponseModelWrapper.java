package nl.tudelft.sem.template.planning.domain.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.template.planning.models.ActivityResponseModel;

@AllArgsConstructor
@Getter
public class ListOfAvailableActivitiesResponseModelWrapper {
    private List<ActivityResponseModel> activityResponseModelList;
}
