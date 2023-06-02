package nl.tudelft.sem.template.planning.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.planning.domain.entity.RoleRequirements;
import nl.tudelft.sem.template.planning.domain.enums.Certificate;
import nl.tudelft.sem.template.planning.domain.enums.Gender;
import nl.tudelft.sem.template.planning.domain.enums.Level;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponseModel {
    private long id;
    private long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endingDate;
    private String description;
    private Certificate certificate;
    private List<RoleRequirements> roles;
    private Gender gender;
    private String organization;
    private Level level;
    private boolean competition;

}
