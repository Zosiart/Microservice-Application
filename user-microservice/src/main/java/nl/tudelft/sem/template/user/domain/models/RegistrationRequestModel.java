package nl.tudelft.sem.template.user.domain.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestModel {
    private String name;
    private Gender genderEnum;
    private String email;
    private List<RoleInfo> roleInfoList;
    private List<Availability> availabilities;
    private Certificate certificate;
    private String organization;
}
