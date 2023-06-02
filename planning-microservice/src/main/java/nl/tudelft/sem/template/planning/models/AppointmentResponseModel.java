package nl.tudelft.sem.template.planning.models;

import lombok.Data;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;
import nl.tudelft.sem.template.planning.domain.entity.UserInfo;

@Data
public class AppointmentResponseModel {
    private final UserInfo user;
    private final Appointment appointment;
}
