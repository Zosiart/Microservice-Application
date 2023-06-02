package nl.tudelft.sem.template.user.domain.attribute.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.user.domain.enums.Certificate;

public class CertificateAttributeConverter implements AttributeConverter<Certificate, String> {
    @Override
    public String convertToDatabaseColumn(Certificate attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Certificate convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Certificate.valueOf(dbData);
    }
}
