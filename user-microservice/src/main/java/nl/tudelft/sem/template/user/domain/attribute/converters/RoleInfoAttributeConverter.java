package nl.tudelft.sem.template.user.domain.attribute.converters;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.enums.RoleEnum;
import nl.tudelft.sem.template.user.domain.enums.SkillLevelEnum;

@SuppressWarnings("PMD")
public class RoleInfoAttributeConverter implements AttributeConverter<List<RoleInfo>, String> {
    @Override
    public String convertToDatabaseColumn(List<RoleInfo> attribute) {
        if (attribute == null || attribute.size() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int size = attribute.size(); // used for making sure there is no comma after the last element
        for (RoleInfo roleInfo : attribute) {
            size--;
            stringBuilder.append(roleInfo.getRole().name()).append(':').append(roleInfo.getLevel().name());
            if (size > 0) {
                stringBuilder.append(',');
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public List<RoleInfo> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.length() == 0) {
            return null;
        }
        String[] split = dbData.split(",");
        List<RoleInfo> roleInfoList = new ArrayList<>();
        for (String s : split) {
            String[] entityJoined = s.split(":");
            roleInfoList.add(new RoleInfo(RoleEnum.valueOf(entityJoined[0]), SkillLevelEnum.valueOf(entityJoined[1])));
        }
        return roleInfoList;
    }
}
