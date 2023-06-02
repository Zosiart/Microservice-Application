package nl.tudelft.sem.template.planning.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nl.tudelft.sem.template.planning.domain.enums.Status;


@Converter
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.toString();
    }

    @Override
    public Status convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Status.valueOf(s);
    }
}
