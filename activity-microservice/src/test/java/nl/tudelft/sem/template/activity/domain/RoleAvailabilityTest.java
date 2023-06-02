package nl.tudelft.sem.template.activity.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.template.activity.domain.activity.Role;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;
import org.junit.jupiter.api.Test;

public class RoleAvailabilityTest {

    @Test
    public void testRoleAvailabilityConstructor() {
        RoleAvailability roleAvailability = new RoleAvailability(Role.Cox, 2);
        assertEquals(Role.Cox, roleAvailability.getRole());
        assertEquals(2, roleAvailability.getRequiredCount());
    }

    @Test
    public void testToString() {
        RoleAvailability roleAvailability = new RoleAvailability(Role.Cox, 2);
        assertEquals("Cox, 2", roleAvailability.toString());
    }

    @Test
    public void testEquals() {
        RoleAvailability roleAvailability = new RoleAvailability(Role.Cox, 2);
        RoleAvailability roleAvailability2 = new RoleAvailability(Role.Cox, 2);
        assertTrue(roleAvailability.equals(roleAvailability2));
    }

    @Test
    public void testEquals2() {
        RoleAvailability roleAvailability = new RoleAvailability(Role.Cox, 2);
        RoleAvailability roleAvailability2 = new RoleAvailability(Role.Coach, 2);
        assertFalse(roleAvailability.equals(roleAvailability2));
    }

    @Test
    public void testEquals3() {
        RoleAvailability roleAvailability = new RoleAvailability(Role.Cox, 2);
        RoleAvailability roleAvailability2 = new RoleAvailability(Role.Cox, 3);
        assertFalse(roleAvailability.equals(roleAvailability2));
    }
}