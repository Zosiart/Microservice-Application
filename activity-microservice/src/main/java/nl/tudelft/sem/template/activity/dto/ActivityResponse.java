package nl.tudelft.sem.template.activity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.activity.domain.activity.Certificate;
import nl.tudelft.sem.template.activity.domain.activity.Gender;
import nl.tudelft.sem.template.activity.domain.activity.Level;
import nl.tudelft.sem.template.activity.domain.activity.RoleAvailability;

@Data
@Builder
@NoArgsConstructor
public class ActivityResponse {
    private int id;
    private int userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endingDate;
    private String description;
    private Certificate certificate;
    private List<RoleAvailability> roles;
    private Gender gender;
    private String organization;
    private Level level;
    private boolean competition;

    /**
     * ActivityResponse data transfer object constructor. Specific to training.
     *
     * @param id            of the activity
     * @param userId        of the activity author
     * @param startingDate  of the activity
     * @param endingDate    of the activity
     * @param description   of the activity
     * @param certificate   needed for the cox if applicable
     * @param roles         needed for the activity
     * @param isCompetition true if the activity is a competition
     */
    public ActivityResponse(int id, int userId, Date startingDate, Date endingDate, String description,
                            Certificate certificate, List<RoleAvailability> roles, boolean isCompetition) {
        this.id = id;
        this.userId = userId;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.description = description;
        this.certificate = certificate;
        this.roles = roles;
        this.competition = isCompetition;
    }

    /**
     * ActivityResponse data transfer object constructor. Specific to competition.
     *
     * @param id            of the activity
     * @param userId        of the activity author
     * @param startingDate  of the activity
     * @param endingDate    of the activity
     * @param description   of the activity
     * @param certificate   needed for the cox if applicable
     * @param roles         needed for the activity
     * @param gender        required
     * @param organization  required
     * @param level         required if applicable
     * @param isCompetition true if the activity is a competition
     */
    public ActivityResponse(int id, int userId, Date startingDate, Date endingDate, String description,
                            Certificate certificate, List<RoleAvailability> roles, Gender gender,
                            String organization, Level level, boolean isCompetition) {
        this.id = id;
        this.userId = userId;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.description = description;
        this.certificate = certificate;
        this.roles = roles;
        this.gender = gender;
        this.organization = organization;
        this.level = level;
        this.competition = isCompetition;
    }

    /**
     * Getter for isCompetition field.
     *
     * @return if the activity is a competition
     */
    public boolean getIsCompetition() {
        return competition;
    }
}
