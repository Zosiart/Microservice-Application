package nl.tudelft.sem.template.activity.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Date;
import nl.tudelft.sem.template.activity.domain.activity.Certificate;
import nl.tudelft.sem.template.activity.domain.activity.Role;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;
import nl.tudelft.sem.template.activity.domain.activity.Training;
import org.junit.jupiter.api.Test;

public class TrainingTest {

    @Test
    public void testTrainingConstructor() {
        Training training = new Training(1, new Date(2022, 12, 17, 16, 30),
            new Date(2022, 12, 17, 18, 30), "This is a description", Certificate.C_FOUR,
            Arrays.asList(new RoleAvailability(Role.Cox, 2), new RoleAvailability(Role.Coach, 1)));
        assertEquals(1, training.getUserId());
        assertEquals(new Date(2022, 12, 17, 16, 30), training.getStartingDate());
        assertEquals(new Date(2022, 12, 17, 18, 30), training.getEndingDate());
        assertEquals("This is a description", training.getDescription());
        assertEquals(Certificate.C_FOUR, training.getCertificate());
        assertEquals(Arrays.asList(new RoleAvailability(Role.Cox, 2), new RoleAvailability(Role.Coach, 1)),
            training.getRoles());
    }

}