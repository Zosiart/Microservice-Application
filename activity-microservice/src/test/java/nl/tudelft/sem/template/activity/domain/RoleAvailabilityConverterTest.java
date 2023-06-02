package nl.tudelft.sem.template.activity.domain;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.activity.domain.activity.Role;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailabilityConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleAvailabilityConverterTest {
    private transient RoleAvailabilityConverter roleAvailabilityConverter;

    @BeforeEach
    void setUp() {
        roleAvailabilityConverter = new RoleAvailabilityConverter();
    }

    @Test
    public void testConvertToDatabaseColumnNull() {
        assertNull(roleAvailabilityConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void testConvertToDatabaseColumnEmpty() {
        assertEquals("", roleAvailabilityConverter.convertToDatabaseColumn(new ArrayList<>()));
    }

    @Test
    void testConvertToDatabaseColumnOneEntry() {
        List<RoleAvailability> roles = new ArrayList<>();
        roles.add(new RoleAvailability(Role.Coach, 1));
        String expected = "Coach,1;";
        assertEquals(expected, roleAvailabilityConverter.convertToDatabaseColumn(roles));
    }

    @Test
    public void testConvertToDatabaseColumnMultipleEntries() {
        List<RoleAvailability> roles = new ArrayList<>();
        roles.add(new RoleAvailability(Role.Coach, 1));
        roles.add(new RoleAvailability(Role.Cox, 1));
        roles.add(new RoleAvailability(Role.PortSideRower, 5));
        String expected = "Coach,1;Cox,1;PortSideRower,5;";
        assertEquals(expected, roleAvailabilityConverter.convertToDatabaseColumn(roles));
    }

    @Test
    public void testConvertToEntityAttributeNull() {
        assertEquals(new ArrayList<>(), roleAvailabilityConverter.convertToEntityAttribute(null));
    }

    @Test
    public void testConvertToEntitySingleRole() {
        List<RoleAvailability> roles = roleAvailabilityConverter.convertToEntityAttribute("Cox,1;");
        assertEquals(1, roles.size());
        assertEquals(Role.Cox, roles.get(0).getRole());
        assertEquals(1, roles.get(0).getRequiredCount());
    }

    @Test
    public void testConvertToEntityMultipleRoles() {
        List<RoleAvailability> roles = roleAvailabilityConverter.convertToEntityAttribute("Coach,1;Cox,1;PortSideRower,5;");
        assertEquals(3, roles.size());
        assertEquals(Role.Coach, roles.get(0).getRole());
        assertEquals(Role.Cox, roles.get(1).getRole());
        assertEquals(Role.PortSideRower, roles.get(2).getRole());
        assertEquals(1, roles.get(0).getRequiredCount());
        assertEquals(1, roles.get(1).getRequiredCount());
        assertEquals(5, roles.get(2).getRequiredCount());
    }




}