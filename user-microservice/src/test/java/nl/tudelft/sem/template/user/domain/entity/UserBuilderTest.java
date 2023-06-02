package nl.tudelft.sem.template.user.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.user.domain.UserBuilder;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.enums.RoleEnum;
import nl.tudelft.sem.template.user.domain.enums.SkillLevelEnum;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;
import org.junit.jupiter.api.Test;

public class UserBuilderTest {
    @Test
    public void createUserTest() throws EmailFormatIncorrectException, UsernameFormatIncorrectException {
        UserBuilder ub = new UserBuilder();
        ub.setName("Sam234567");
        ub.setEmail(new Email("sam@sem.com"));
        ub.setGender(Gender.NON_BINARY);
        List<RoleInfo> roleInfoList = new ArrayList<>();
        RoleInfo r1 = new RoleInfo(RoleEnum.ScullingRower, SkillLevelEnum.Amateur);
        RoleInfo r2 = new RoleInfo(RoleEnum.PortSideRower, SkillLevelEnum.Competitive);
        roleInfoList.add(r1);
        roleInfoList.add(r2);
        ub.setRoles(roleInfoList);
        List<Availability> schedule = new ArrayList<>();
        Date d1 = new Date(2022, 3, 20);
        Date d2 = new Date(2022, 7, 14);
        Availability availability = new Availability(d1, d2);
        schedule.add(availability);
        ub.setSchedule(schedule);

        User user = ub.build();
        assertEquals("Sam234567", user.getName());
        assertEquals("sam@sem.com", user.getEmail().getEmailText());
        assertEquals(Gender.NON_BINARY, user.getGender());
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(r1));
        assertTrue(user.getRoles().contains(r2));
        assertEquals(1, user.getAvailabilities().size());
        assertTrue(user.getAvailabilities().contains(availability));
    }
}
