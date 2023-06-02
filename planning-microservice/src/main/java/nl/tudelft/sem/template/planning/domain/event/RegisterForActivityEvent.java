package nl.tudelft.sem.template.planning.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.tudelft.sem.template.planning.domain.enums.Role;

@AllArgsConstructor
@Getter
public class RegisterForActivityEvent {
    Long activityId;
    Long applicantUserId;
    Role applicantRole;
}
