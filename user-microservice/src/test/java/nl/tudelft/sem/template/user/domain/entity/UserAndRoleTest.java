package nl.tudelft.sem.template.user.domain.entity;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.user.domain.enums.Certificate;
import nl.tudelft.sem.template.user.domain.enums.Gender;
import nl.tudelft.sem.template.user.domain.enums.RoleEnum;
import nl.tudelft.sem.template.user.domain.enums.SkillLevelEnum;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import nl.tudelft.sem.template.user.domain.exceptions.UsernameFormatIncorrectException;
import org.junit.jupiter.api.Test;

public class UserAndRoleTest {

    @Test
    public void roleToString() {
        RoleInfo roleInfo = new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Amateur);
        assertEquals("RoleInfo{role=Coach, level=Amateur}", roleInfo.toString());
    }

    @Test
    public void userToString() throws EmailFormatIncorrectException, UsernameFormatIncorrectException {
        User user = new User("JohnRower",
            Gender.MALE);
        assertEquals("User{name='JohnRower', gender=MALE}", user.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 12, 25, 12, 30, 0);
        Date start = calendar.getTime();
        calendar.set(2022, 12, 25, 14, 30, 0);
        Date end = calendar.getTime();
        User user2 = new User("abcdef123", new Email("abc@yahoo.com"),
                Gender.MALE, List.of(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Competitive)),
                List.of(new Availability(start, end)), Certificate.EIGHT_PLUS, "Laga");
        String result = user2.toString();
        //assertEquals("User{name='abcdef123',
        // email=abc@yahoo.com, gender=MALE, roles=RoleInfo{role=Coach, level=Competitive}
        // available on:Availabilities{startTime=Wed Jan 25 12:30:00 EET 2023, endTime=Wed Jan 25 14:30:00 EET 2023},
        // organization=Laga}",
        //        user2.toString());
        assertTrue(result.contains("name='abcdef123'"));
        assertTrue(result.contains("email=abc@yahoo.com"));
        assertTrue(result.contains("gender=MALE"));
        assertTrue(result.contains("role=Coach, level=Competitive"));
        assertTrue(result.contains("organization=Laga"));
    }

    @Test
    public void equalUsers() throws EmailFormatIncorrectException, UsernameFormatIncorrectException {
        User u1 = new User("zzz12345zzz", Gender.MALE);
        User u2 = new User("zzz12345zzz", Gender.MALE);
        User u3 = new User("abcDEF_123a", new Email("abc@yahoo.com"),
                Gender.FEMALE, null, null,
                Certificate.EIGHT_PLUS, "Laga");
        User u4 = new User("abcDEF_123a", new Email("abc@yahoo.com"),
                Gender.FEMALE, null, null,
                Certificate.EIGHT_PLUS, "Laga");
        assertTrue(u1.equals(u2));
        assertTrue(u3.equals(u4));
        assertTrue(u1.equals(u1));
    }

    @Test
    public void differentUsers() throws EmailFormatIncorrectException, UsernameFormatIncorrectException {
        User u1 = new User();
        User u2 = new User("ffz_ABCDEF", Gender.NON_BINARY);
        User u3 = new User("ffz_ABCDEF", new Email("ffz@gmail.com"),
                Gender.NON_BINARY, List.of(new RoleInfo(RoleEnum.ScullingRower, SkillLevelEnum.Amateur)),
                null, Certificate.EIGHT_PLUS, "Laga");
        User u4 = new User("ffz_ABCDEF", new Email("ffz@gmail.com"),
                Gender.NON_BINARY, List.of(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Amateur)),
                null, Certificate.EIGHT_PLUS, "Laga");
        assertFalse(u1.equals(u2));
        assertFalse(u2.equals(u3));
        assertFalse(u3.equals(u4));
        User u5 = new User("ffz_ABCDEF", new Email("ffz@gmail.com"),
                Gender.NON_BINARY, List.of(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Amateur)),
                List.of(new Availability(new Date(100000L), new Date(100500))), Certificate.EIGHT_PLUS, "Laga");
        assertFalse(u4.equals(u5));
        assertFalse(u1.equals(null));
        assertFalse(u1.equals(new Email()));
    }

    @Test
    public void validUsernames() {
        assertTrue(User.validUsername("abcdef1234"));
        assertTrue(User.validUsername("abc_123_abc"));
        assertTrue(User.validUsername("abc.def.ghi.234"));
        assertTrue(User.validUsername("abc.123abc"));
    }

    @Test
    public void usernameStartsWithNonAlphaNumeric() {
        assertFalse(User.validUsername("_abcdefghi23"));
        assertFalse(User.validUsername(".abcdhwf45jrvs"));
        assertFalse(User.validUsername("!abcdhwf45jrvs"));
    }

    @Test
    public void userNameEndsWithNonAlphaNumeric() {
        assertFalse(User.validUsername("abcdefghi23_"));
        assertFalse(User.validUsername("abcdhwf45jrvs!"));
        assertFalse(User.validUsername("abcdhwf45jrvs."));
    }

    @Test
    public void lessThan8Characters() {
        assertFalse(User.validUsername("user"));
    }

    @Test
    public void exactly8Characters() {
        assertTrue(User.validUsername("useruser"));
    }

    @Test
    public void between8and20Characters() {
        assertTrue(User.validUsername("useruser12345"));
    }

    @Test
    public void exactly20Characters() {
        assertTrue(User.validUsername("usernameusername1234"));
    }

    @Test
    public void moreThan20Characters() {
        assertFalse(User.validUsername("hbxgjdgbexjxgexkgajfbwkgzkwgbzkgjvnw"));
    }

}
