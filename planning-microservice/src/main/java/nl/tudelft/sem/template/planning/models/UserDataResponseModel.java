package nl.tudelft.sem.template.planning.models;

import java.util.List;
import lombok.*;
import nl.tudelft.sem.template.planning.domain.entity.Availability;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.enums.Certificate;
import nl.tudelft.sem.template.planning.domain.enums.Gender;


@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserDataResponseModel {
    private Long id;
    private List<RoleInfo> roles;
    private List<Availability> availabilities;
    private Certificate certificate;
    private Gender gender;
    private String organization;
}
