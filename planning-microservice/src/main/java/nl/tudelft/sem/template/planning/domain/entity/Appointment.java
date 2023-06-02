package nl.tudelft.sem.template.planning.domain.entity;

import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.planning.domain.HasEvents;
import nl.tudelft.sem.template.planning.domain.converter.RoleConverter;
import nl.tudelft.sem.template.planning.domain.converter.StatusConverter;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import nl.tudelft.sem.template.planning.domain.event.RegisterForActivityEvent;
import nl.tudelft.sem.template.planning.domain.event.UpdateAppointmentStatusEvent;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class Appointment extends HasEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "activity_id", nullable = false)
    private long activityId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "role", nullable = false)
    @Convert(converter = RoleConverter.class)
    private Role role;


    @Column(name = "status", nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    /**
     * Create a new appointment.
     *
     * @param activityId - the id of the activity
     * @param userId     - the id of the user
     * @param role       - the role of the user
     */
    public Appointment(long activityId, long userId, Role role) {
        this.activityId = activityId;
        this.userId = userId;
        this.role = role;
        this.status = Status.PENDING;
        this.recordThat(new RegisterForActivityEvent(activityId, userId, role));
    }

    public void attachNotifier() {
        this.recordThat(new UpdateAppointmentStatusEvent(activityId, userId, role, status));
    }


    /**
     * Equality is only based on the identifier.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Appointment appointment = (Appointment) o;
        return id == appointment.id;
    }

    public int hashCode() {
        return Objects.hash(id);
    }

}
