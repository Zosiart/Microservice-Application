package nl.tudelft.sem.template.planning.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.planning.domain.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequirements {

    private Role role;
    private Integer requiredCount;
}
