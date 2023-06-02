package nl.tudelft.sem.template.planning.domain.entity;

import lombok.*;
import nl.tudelft.sem.template.planning.domain.enums.Level;
import nl.tudelft.sem.template.planning.domain.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleInfo {
    private Role role;
    private Level level;
}
