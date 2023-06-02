package nl.tudelft.sem.template.user.domain.attribute.converters;

import static org.junit.jupiter.api.Assertions.*;

import nl.tudelft.sem.template.user.domain.entity.Email;
import nl.tudelft.sem.template.user.domain.exceptions.EmailFormatIncorrectException;
import org.junit.jupiter.api.Test;

class EmailAttributeConverterTest {

    @Test
    void convertToDatabaseColumn() throws Exception {
        EmailAttributeConverter emailAttributeConverter = new EmailAttributeConverter();
        assertNull(emailAttributeConverter.convertToDatabaseColumn(null));
        assertEquals("example@yahoo.com", emailAttributeConverter.convertToDatabaseColumn(new Email("example@yahoo.com")));
    }

    @Test
    void convertToEntityAttribute() {
        EmailAttributeConverter emailAttributeConverter = new EmailAttributeConverter();
        assertNull(emailAttributeConverter.convertToEntityAttribute(null));
        assertEquals("example2@yahoo.com",
            emailAttributeConverter.convertToEntityAttribute("example2@yahoo.com").getEmailText());
        assertThrows(EmailFormatIncorrectException.class, () -> emailAttributeConverter.convertToEntityAttribute("invalid"));
    }
}