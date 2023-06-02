package nl.tudelft.sem.template.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
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
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserBuilderTest {
    private UserBuilder userBuilder;

    @BeforeEach
    void setUp() {
        userBuilder = new UserBuilder();
    }

    @Test
    void setEmail() throws Exception {
        userBuilder.setName("basicusername");
        userBuilder.setEmail(new Email("abcd@gmail.com"));
        User user = userBuilder.build();
        assertEquals("abcd@gmail.com", user.getEmail().getEmailText());
    }

    @Test
    void setNameValid() {
        userBuilder.setName("abcdef999");
        User user = userBuilder.build();
        assertEquals("abcdef999", user.getName());
    }

    @Test
    void setGender() {
        userBuilder.setName("basicusername");
        userBuilder.setGender(Gender.FEMALE);
        User user = userBuilder.build();
        assertEquals(Gender.FEMALE, user.getGender());
    }

    @Test
    void setRoles() throws UsernameFormatIncorrectException {
        userBuilder.setName("basicusername");
        List<RoleInfo> roles = new ArrayList<>();
        roles.add(new RoleInfo(RoleEnum.Cox, SkillLevelEnum.Amateur));
        userBuilder.setRoles(roles);
        assertEquals(roles, userBuilder.build().getRoles());
    }

    @Test
    void setSchedule() {
        userBuilder.setName("basicusername");
        List<Availability> schedule = new ArrayList<>();
        schedule.add(new Availability(new Date(2023, 2, 4), new Date(2022, 7, 14)));
        userBuilder.setSchedule(schedule);
        assertEquals(schedule, userBuilder.build().getAvailabilities());
    }

    @Test
    void setCertificate()  {
        userBuilder.setName("basicusername");
        userBuilder.setCertificate(Certificate.C_FOUR);
        assertEquals(Certificate.C_FOUR, userBuilder.build().getCertificate());
    }

    @Test
    void setOrganization() {
        userBuilder.setName("basicusername");
        userBuilder.setOrganization("Laga");
        assertEquals("Laga", userBuilder.build().getOrganization());
    }

}