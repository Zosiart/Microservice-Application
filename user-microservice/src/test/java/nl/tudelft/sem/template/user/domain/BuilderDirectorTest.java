package nl.tudelft.sem.template.user.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.entity.User;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.enums.RoleEnum;
import nl.tudelft.sem.template.user.domain.enums.SkillLevelEnum;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import org.junit.jupiter.api.Test;

class BuilderDirectorTest {

    @Test
    void constructBasicUser() {
        UserBuilder userBuilder = new UserBuilder();
        String name = "AbcdEfgh";
        Gender gender = Gender.MALE;
        User basicUser = new BuilderDirector().constructBasicUser(userBuilder, name, gender);
        assertEquals(name, basicUser.getName());
        assertEquals(gender, basicUser.getGender());
        assertNull(basicUser.getOrganization());
        assertNull(basicUser.getCertificate());
        assertNull(basicUser.getAvailabilities());
        assertNull(basicUser.getRoles());
        assertNull(basicUser.getEmail());
    }

    @Test
    void constructCompleteUser() throws EmailFormatIncorrectException {
        UserBuilder userBuilder = new UserBuilder();
        String name = "AbcdEfgh";
        Gender gender = Gender.MALE;
        String organization = "Rowing Association";
        Certificate certificateEnum = Certificate.C_FOUR;
        List<Availability> availabilities = List.of(new Availability(new Date(9000100L), new Date(9000150L)));
        List<RoleInfo> roles = List.of(new RoleInfo(RoleEnum.PortSideRower, SkillLevelEnum.Amateur));
        Email email = new Email("abcd@gmail.com");
        User completeUser = new BuilderDirector().constructCompleteUser(userBuilder, name, gender,
                email, roles, availabilities, certificateEnum, organization);
        assertEquals(name, completeUser.getName());
        assertEquals(gender, completeUser.getGender());
        assertEquals(organization, completeUser.getOrganization());
        assertEquals(certificateEnum, completeUser.getCertificate());
        assertEquals(availabilities, completeUser.getAvailabilities());
        assertEquals(roles, completeUser.getRoles());
        assertEquals(email, completeUser.getEmail());
    }
}