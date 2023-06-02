package nl.tudelft.sem.template.user.domain.attribute.converters;

import javax.persistence.AttributeConverter;
import lombok.SneakyThrows;
import nl.tudelft.sem.template.user.domain.entity.Email;

public class EmailAttributeConverter implements AttributeConverter<Email, String> {
    @Override
    public String convertToDatabaseColumn(Email attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getEmailText();
    }

    @SneakyThrows
    @Override
    public Email convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new Email(dbData);
    }
}
