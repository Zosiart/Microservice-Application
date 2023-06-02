package nl.tudelft.sem.template.activity.domain.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DDD entity representing an application activity in our domain.
 */
@Entity
@Table(name = "activities")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activity_type")
@Getter
@Setter
public abstract class Activity {

    /**
     * Identifier for the application activities.
     */
    @Id
    @SequenceGenerator(name = "activity_id_seq", sequenceName = "activity_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_id_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * User id of the author.
     */
    @NotNull
    @Column(name = "user_id", nullable = false)
    private int userId;

    /**
     * The start date of the activity.
     */
    @NotNull
    @Column(name = "starting_date", nullable = false)
    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startingDate;

    /**
     * The end date of the activity.
     */
    @NotNull
    @Column(name = "ending_date", nullable = false)
    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endingDate;

    /**
     * Description of the activity.
     */
    @NotNull
    @Column(name = "description")
    private String description;

    /**
     * Certificate in case the activity needs a cox.
     */
    @Column(name = "certificate")
    private Certificate certificate;

    /**
     * Roles needed for the activity.
     */
    @NotNull
    @Column(name = "roles", nullable = false)
    @Convert(converter = RoleAvailabilityConverter.class)
    private List<RoleAvailability> roles;

    /**
     * Activity constructor.
     *
     * @param userId of the author
     * @param startingDate of the activity
     * @param endingDate of the activity
     * @param description of the activity
     * @param certificate of the activity, if COX is included
     * @param roles needed for the activity
     */
    public Activity(Integer userId, Date startingDate, Date endingDate, String description,
                    Certificate certificate, List<RoleAvailability> roles) {
        this.userId = userId;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.description = description;
        this.certificate = certificate;
        this.roles = roles;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Activity activity = (Activity) o;
        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, startingDate, endingDate, description, certificate, roles);
    }
}
