package nl.tudelft.sem.template.user.domain.attribute.converters;


import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.user.domain.enums.Gender;

public class GenderAttributeConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute.name();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return Gender.valueOf(dbData);
    }
}
