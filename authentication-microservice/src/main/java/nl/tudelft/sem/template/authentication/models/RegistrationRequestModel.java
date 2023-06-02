package nl.tudelft.sem.template.authentication.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.authentication.domain.user.entity.Availability;
import nl.tudelft.sem.template.authentication.domain.user.entity.RoleInfo;
import nl.tudelft.sem.template.authentication.domain.user.enums.CertificateEnum;
import nl.tudelft.sem.template.authentication.domain.user.enums.Gender;

/**
 * Model representing a registration request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestModel {
    private String username;
    private String password;
    private Gender gender;
    private String email;
    private List<RoleInfo> roleInfoList;
    private List<Availability> availabilities;
    private CertificateEnum certificate;
    private String organization;
}