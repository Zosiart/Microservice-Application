package nl.tudelft.sem.template.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.user.domain.enums.RoleEnum;
import nl.tudelft.sem.template.user.domain.enums.SkillLevelEnum;

@AllArgsConstructor
@NoArgsConstructor
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
