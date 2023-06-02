package nl.tudelft.sem.template.user.domain.attribute.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import nl.tudelft.sem.template.user.domain.enums.Certificate;
import org.junit.jupiter.api.Test;

class CertificateAttributeConverterTest {

    @Test
    void convertToDatabaseColumn() {
        CertificateAttributeConverter certificateAttributeConverter = new CertificateAttributeConverter();
        assertNull(certificateAttributeConverter.convertToDatabaseColumn(null));
        assertEquals("FOUR_PLUS", certificateAttributeConverter.convertToDatabaseColumn(Certificate.FOUR_PLUS));
    }

    @Test
    void convertToEntityAttribute() {
        CertificateAttributeConverter certificateAttributeConverter = new CertificateAttributeConverter();
        assertNull(certificateAttributeConverter.convertToEntityAttribute(null));
        assertEquals(Certificate.C_FOUR, certificateAttributeConverter.convertToEntityAttribute("C_FOUR"));
    }
}