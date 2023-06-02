package nl.tudelft.sem.template.planning.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import nl.tudelft.sem.template.planning.domain.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatusConvertTest {
    private StatusConverter statusConverter;

    @BeforeEach
    void setUp() {
        statusConverter = new StatusConverter();
    }

    @Test
    public void testConvertToDatabaseColumnNull() {
        assertNull(statusConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void testConvertToEntityAttributeNull() {
        assertNull(statusConverter.convertToEntityAttribute(null));
    }

    @Test
    public void testConvertToDatabaseColumn() {
        assertEquals("PENDING", statusConverter.convertToDatabaseColumn(Status.PENDING));
    }

    @Test
    public void testConvertToEntityAttribute() {
        assertEquals(Status.PENDING, statusConverter.convertToEntityAttribute("PENDING"));
    }
}
