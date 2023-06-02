package nl.tudelft.sem.template.activity.database;

import java.util.List;
import nl.tudelft.sem.template.activity.domain.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query(value = "SELECT * FROM activities a WHERE (:ownerId IS NULL OR a.user_id = :ownerId) "
        + "AND (a.starting_date >= :start_time AND a.ending_date <= :end_time) "
        + "AND ((a.activity_type = 'Training') OR (a.activity_type = 'Competition' "
        + "AND (:gender IS NULL OR a.gender = :gender) AND (:organization IS NULL OR a.organization = :organization)))",
        nativeQuery = true)
    List<Activity> findByFilters(@Param("ownerId") Long ownerId,
                                 @Param("start_time") String startTime, @Param("end_time") String endTime,
                                 @Param("gender") Integer gender,
                                 @Param("organization") String organization);

    List<Activity> findActivitiesByUserId(int userId);
}