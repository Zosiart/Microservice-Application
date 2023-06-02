package nl.tudelft.sem.template.activity.domain;

import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.activity.domain.activity.Availability;

public class ActivityFilters {

    /**
     * If any of the dates the user is available is in between the starting and ending date of the activity, return true.
     *
     * @param availabilities dates the user is available
     * @param startingDate   of activity
     * @param endingDate     of activity
     * @return true if user is available during any period of the activity
     */
    public static boolean filterByAvailability(List<Availability> availabilities, Date startingDate, Date endingDate) {
        if (availabilities.isEmpty()) {
            return true;
        }

        return availabilities.stream()
            .anyMatch(a -> !a.getStartTime().after(startingDate) && !a.getEndTime().before(endingDate));
    }


}
