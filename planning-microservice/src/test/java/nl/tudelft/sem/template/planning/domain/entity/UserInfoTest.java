package nl.tudelft.sem.template.planning.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import nl.tudelft.sem.template.planning.domain.enums.Certificate;
import nl.tudelft.sem.template.planning.domain.enums.Gender;
import nl.tudelft.sem.template.planning.models.UserDataResponseModel;
import nl.tudelft.sem.template.planning.models.UserResponseModel;
import org.junit.jupiter.api.Test;

public class UserInfoTest {

    @Test
    public void testFromUserResponseModel() {
        UserDataResponseModel userResponseModel = new UserDataResponseModel();
        userResponseModel.setId(1L);
        userResponseModel.setCertificate(Certificate.C_FOUR);
        userResponseModel.setGender(Gender.FEMALE);
        userResponseModel.setRoles(List.of(new RoleInfo()));

        assertEquals(1, UserInfo.fromUserResponseModel(userResponseModel).getId());
        assertEquals(Certificate.C_FOUR, UserInfo.fromUserResponseModel(userResponseModel).getCertificate());
        assertEquals(Gender.FEMALE, UserInfo.fromUserResponseModel(userResponseModel).getGender());
        assertEquals(List.of(new RoleInfo()), UserInfo.fromUserResponseModel(userResponseModel).getRoles());
    }
}
