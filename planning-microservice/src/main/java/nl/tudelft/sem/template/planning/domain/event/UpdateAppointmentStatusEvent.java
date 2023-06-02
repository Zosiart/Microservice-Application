package nl.tudelft.sem.template.planning.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;

@AllArgsConstructor
@Getter
public class UpdateAppointmentStatusEvent {
    Long activityId;
    Long applicantUserId;
    Role applicantRole;
    Status status;
}
