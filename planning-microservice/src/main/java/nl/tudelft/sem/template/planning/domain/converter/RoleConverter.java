package nl.tudelft.sem.template.planning.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nl.tudelft.sem.template.planning.domain.enums.Role;

@Converter
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return role.toString();
    }

    @Override
    public Role convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Role.valueOf(s);
    }
}
