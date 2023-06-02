package nl.tudelft.sem.template.authentication.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.tudelft.sem.template.authentication.domain.user.enums.RoleEnum;
import nl.tudelft.sem.template.authentication.domain.user.enums.SkillLevelEnum;

@AllArgsConstructor
@Getter
@Setter
public class RoleInfo {
    RoleEnum role;
    SkillLevelEnum level;

    @Override
    public String toString() {
        return "RoleInfo{"
            + "role=" + role.name()
            + ", level=" + level.name()
            + '}';
    }
}
