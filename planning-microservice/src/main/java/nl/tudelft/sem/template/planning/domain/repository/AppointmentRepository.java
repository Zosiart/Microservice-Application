package nl.tudelft.sem.template.planning.domain.repository;

import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.planning.domain.entity.Appointment;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import nl.tudelft.sem.template.planning.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    /**
     * Find appointment by id.
     */
    Optional<Appointment> findById(long appointmentId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.activityId = ?1 AND a.role = ?2 AND a.status = ?3")
    int countStatusForActivityRole(long activityId, Role role, Status status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a "
            + "WHERE a.userId = ?1 AND a.activityId = ?2 AND a.status = ?3")
    boolean userHasStatusForActivity(long userId, long activityId, Status status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a "
            + "WHERE a.userId = ?1 AND a.activityId = ?2 AND a.role = ?3")
    boolean userHasAppointmentForActivityRole(long userId, long activityId, Role role);

    List<Appointment> findAllByUserId(long userId);

    List<Appointment> findAllByUserIdAndStatus(long userId, Status status);

    List<Appointment> findAllByActivityIdInAndStatus(List<Long> activityIds, Status status);
}
