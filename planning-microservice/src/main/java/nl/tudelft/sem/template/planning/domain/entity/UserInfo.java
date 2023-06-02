package nl.tudelft.sem.template.planning.domain.entity;

import java.util.List;
import lombok.Data;
import nl.tudelft.sem.template.planning.domain.enums.Certificate;
import nl.tudelft.sem.template.planning.domain.enums.Gender;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;

@Data
public class UserInfo {

    private Long id;

    private Gender gender;

    private List<RoleInfo> roles;

    private Certificate certificate;

    /**
     * Constructor UserInfo from UserResponseModel.
     *
     * @param userResponseModel - the user response model
     */
    public static UserInfo fromUserResponseModel(UserDataResponseModel userResponseModel) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userResponseModel.getId());
        userInfo.setGender(userResponseModel.getGender());
        userInfo.setRoles(userResponseModel.getRoles());
        userInfo.setCertificate(userResponseModel.getCertificate());
        return userInfo;
    }
}
