package nl.tudelft.sem.template.planning.models;

import java.util.List;
import lombok.Data;
import nl.tudelft.sem.template.planning.domain.entity.RoleAndCount;

@Data
public class AvailableActivitiesResponseModel {
    private int activityId;
    private List<RoleAndCount> roles;
}
