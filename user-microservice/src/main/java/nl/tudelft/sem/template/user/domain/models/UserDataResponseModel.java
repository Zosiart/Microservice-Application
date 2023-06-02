package nl.tudelft.sem.template.user.domain.models;

import java.util.List;
import lombok.*;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;

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

    /**
     * Creates a UserDataResponseModel from a User.
     *
     * @param user - the user to be converted
     * @return UserDataResponseModel
     */
    public static UserDataResponseModel fromUser(User user) {
        return new UserDataResponseModel(
                user.getId(),
                user.getRoles(),
                user.getAvailabilities(),
                user.getCertificate(),
                user.getGender(),
                user.getOrganization()
        );
    }
}
