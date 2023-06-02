package nl.tudelft.sem.template.planning.models;

import lombok.Data;
import nl.tudelft.sem.template.planning.domain.enums.Role;

@Data
public class NewAppointmentRequestModel {
    private int activityId;
    private Role role;
}
