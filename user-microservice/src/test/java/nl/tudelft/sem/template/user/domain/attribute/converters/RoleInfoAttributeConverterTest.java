package nl.tudelft.sem.template.user.domain.attribute.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.user.domain.entity.RoleInfo;
import nl.tudelft.sem.template.user.domain.enums.RoleEnum;
import nl.tudelft.sem.template.user.domain.enums.SkillLevelEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RoleInfoAttributeConverterTest {

    private RoleInfoAttributeConverter roleInfoAttributeConverter;

    @BeforeEach
    void setUp() {
        roleInfoAttributeConverter = new RoleInfoAttributeConverter();
    }

    @Test
    void convertToDatabaseColumnNull() {
        assertNull(roleInfoAttributeConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void convertToDatabaseColumnEmpty() {
        assertNull(roleInfoAttributeConverter.convertToDatabaseColumn(new ArrayList<>()));
    }

    @Test
    void convertToDatabaseColumnOneEntry() {
        List<RoleInfo> roles = new ArrayList<>();
        roles.add(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Competitive));
        String expected = "Coach:Competitive";
        assertEquals(expected, roleInfoAttributeConverter.convertToDatabaseColumn(roles));
    }

    @Test
    public void convertToDatabaseColumnMultipleEntries() {
        List<RoleInfo> roles = new ArrayList<>();
        roles.add(new RoleInfo(RoleEnum.Coach, SkillLevelEnum.Competitive));
        roles.add(new RoleInfo(RoleEnum.ScullingRower, SkillLevelEnum.Amateur));
        roles.add(new RoleInfo(RoleEnum.Cox, SkillLevelEnum.Competitive));
        String expected = "Coach:Competitive,ScullingRower:Amateur,Cox:Competitive";
        assertEquals(expected, roleInfoAttributeConverter.convertToDatabaseColumn(roles));
    }

    @Test
    public void convertToEntityAttributeNull() {
        assertNull(roleInfoAttributeConverter.convertToEntityAttribute(null));
    }

    @Test
    public void convertToEntityAttributeEmptyString() {
        assertNull(roleInfoAttributeConverter.convertToEntityAttribute(""));
    }

    @Test
    public void convertToEntityMultipleRoles() {
        List<RoleInfo> roles = roleInfoAttributeConverter.convertToEntityAttribute("Cox:Amateur,Coach:Competitive");
        assertEquals(2, roles.size());
        assertEquals(RoleEnum.Cox, roles.get(0).getRole());
        assertEquals(RoleEnum.Coach, roles.get(1).getRole());
        assertEquals(SkillLevelEnum.Amateur, roles.get(0).getLevel());
        assertEquals(SkillLevelEnum.Competitive, roles.get(1).getLevel());
    }
}