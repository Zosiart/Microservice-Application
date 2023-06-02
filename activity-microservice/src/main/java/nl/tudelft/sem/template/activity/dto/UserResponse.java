package nl.tudelft.sem.template.activity.dto;

import java.util.List;
import lombok.*;
import nl.tudelft.sem.template.activity.domain.activity.Availability;
import nl.tudelft.sem.template.activity.domain.activity.Certificate;
import nl.tudelft.sem.template.activity.domain.activity.Gender;
import nl.tudelft.sem.template.activity.domain.activity.RoleInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private List<RoleInfo> roles;
    private List<Availability> availabilities;
    private Certificate certificate;
    private Gender gender;
    private String organization;
}
