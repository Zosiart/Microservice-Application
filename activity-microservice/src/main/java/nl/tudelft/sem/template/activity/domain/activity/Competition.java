package nl.tudelft.sem.template.activity.domain.activity;

import com.sun.istack.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Competition")
@DiscriminatorValue("Competition")
@NoArgsConstructor
@Getter
@Setter
public class Competition extends Activity {

    /**
     * Required gender for the competition.
     */
    @NotNull
    @Column(name = "gender", nullable = true)
    private Gender gender;

    /**
     * Required organization for the competition.
     */
    @NotNull
    @Column(name = "organization", nullable = true)
    private String organization;

    /**
     * Required level for the competition.
     */
    @Column(name = "level", nullable = true)
    private Level level;

    /**
     * Competition activity constructor.
     *
     * @param userId of the author
     * @param startingDate of the competition
     * @param endingDate of the competition
     * @param description of the competition
     * @param certificate needed for the cox if applicable
     * @param roles needed for the competition
     * @param gender required for the participants
     * @param organization required for the participants
     * @param level required for the participants
     */
    public Competition(Integer userId, Date startingDate, Date endingDate, String description,
                       Certificate certificate, List<RoleAvailability> roles, Gender gender,
                       String organization, Level level) {
        super(userId, startingDate, endingDate, description, certificate, roles);
        this.gender = gender;
        this.organization = organization;
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Competition)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Competition that = (Competition) o;
        return gender == that.gender && organization.equals(that.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gender, organization);
    }
}
