package nl.tudelft.sem.template.activity.domain.activity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleInfo {
    private Role role;
    private Level level;
}
