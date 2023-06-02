package nl.tudelft.sem.template.planning.domain.converter;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import nl.tudelft.sem.template.planning.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleConverterTest {
    private RoleConverter roleConverter;

    @BeforeEach
    void setUp() {
        roleConverter = new RoleConverter();
    }

    @Test
    public void testConvertToDatabaseColumnNull() {
        assertNull(roleConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void testConvertToEntityAttributeNull() {
        assertNull(roleConverter.convertToEntityAttribute(null));
    }

    @Test
    public void testConvertToDatabaseColumn() {
        assertEquals("Cox", roleConverter.convertToDatabaseColumn(Role.Cox));
    }

    @Test
    public void testConvertToEntityAttribute() {
        assertEquals(Role.Cox, roleConverter.convertToEntityAttribute("Cox"));
    }
}
