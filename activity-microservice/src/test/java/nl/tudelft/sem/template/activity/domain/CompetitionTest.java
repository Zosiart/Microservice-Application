package nl.tudelft.sem.template.activity.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Date;
import nl.tudelft.sem.template.activity.domain.activity.Certificate;
import nl.tudelft.sem.template.activity.domain.activity.Competition;
import nl.tudelft.sem.template.activity.domain.activity.Gender;
import nl.tudelft.sem.template.activity.domain.activity.Level;
import nl.tudelft.sem.template.activity.domain.activity.Role;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;
import org.junit.jupiter.api.Test;

public class CompetitionTest {

    @Test
    public void testCompetitionConstructor() {
        Competition competition = new Competition(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 17, 18, 30), "This is a description",
            Certificate.C_FOUR, Arrays.asList(new RoleAvailability(Role.Cox, 2),
            new RoleAvailability(Role.Coach, 1)), Gender.FEMALE, "TUD", Level.Amateur);
        assertEquals(1, competition.getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), competition.getStartingDate());
        assertEquals(new Date(2022, 12, 17, 18, 30), competition.getEndingDate());
        assertEquals("This is a description", competition.getDescription());
        assertEquals(Certificate.C_FOUR, competition.getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Cox, 2), new RoleAvailability(Role.Coach,
                1)),
            competition.getRoles());
        assertEquals(Gender.FEMALE, competition.getGender());
        assertEquals("TUD", competition.getOrganization());
        assertEquals(Level.Amateur, competition.getLevel());
    }


}