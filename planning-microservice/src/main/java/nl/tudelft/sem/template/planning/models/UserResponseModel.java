package nl.tudelft.sem.template.planning.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.enums.Certificate;
import nl.tudelft.sem.template.planning.domain.enums.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseModel {

    private Integer id;

    private Gender gender;

    private List<RoleInfo> roles;

    private Certificate certificate;

    private String email;

}
