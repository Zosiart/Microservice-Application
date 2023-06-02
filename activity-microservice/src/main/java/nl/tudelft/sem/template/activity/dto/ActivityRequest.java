package nl.tudelft.sem.template.activity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.*;
import nl.tudelft.sem.template.activity.domain.activity.Certificate;
import nl.tudelft.sem.template.activity.domain.activity.Gender;
import nl.tudelft.sem.template.activity.domain.activity.Level;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityRequest {
    private Integer userId;

    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startingDate;

    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endingDate;

    private String description;
    private Certificate certificate;
    private List<RoleAvailability> roles;
    private Gender gender;
    private String organization;
    private Level level;
    private boolean competition;
}
