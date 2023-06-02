package nl.tudelft.sem.template.activity.domain.activity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the RoleInfo value object.
 */
@Converter
public class RoleAvailabilityConverter implements AttributeConverter<List<RoleAvailability>, String> {
    @Override
    public String convertToDatabaseColumn(List<RoleAvailability> attribute) {
        if (attribute == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (RoleAvailability roleAvailability : attribute) {
            sb.append(roleAvailability.getRole().toString()).append(',')
                    .append(roleAvailability.getRequiredCount()).append(';');
        }
        return sb.toString();
    }

    @Override
    public List<RoleAvailability> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new ArrayList<>();
        }
        String[] roleAvailabilities = dbData.split(";");
        List<RoleAvailability> roleAvailabilityList = new ArrayList<>();
        for (String roleAvailability : roleAvailabilities) {
            String[] roleAvailabilitySplit = roleAvailability.split(",");
            Role role = Role.valueOf(roleAvailabilitySplit[0]);
            int numSpot = Integer.parseInt(roleAvailabilitySplit[1]);
            roleAvailabilityList.add(new RoleAvailability(role, numSpot));
        }
        return roleAvailabilityList;
    }
}
