package nl.tudelft.sem.template.user.domain.attribute.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.template.user.domain.enums.Gender;
import org.junit.jupiter.api.Test;

class GenderAttributeConverterTest {

    @Test
    void convertToDatabaseColumn() {
        assertEquals("MALE", new GenderAttributeConverter().convertToDatabaseColumn(Gender.MALE));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(Gender.FEMALE, new GenderAttributeConverter().convertToEntityAttribute("FEMALE"));
    }
}