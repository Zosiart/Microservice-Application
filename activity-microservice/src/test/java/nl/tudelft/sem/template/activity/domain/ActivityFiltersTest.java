package nl.tudelft.sem.template.activity.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.template.activity.domain.activity.Availability;
import org.junit.jupiter.api.Test;

public class ActivityFiltersTest {

    @Test
    public void filterByAvailability() {
        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability(new Date(2020, 1, 1), new Date(2020, 1, 2)));
        availabilities.add(new Availability(new Date(2020, 1, 3), new Date(2020, 1, 4)));

        Date startingDate = new Date(2020, 1, 1);
        Date endingDate = new Date(2020, 1, 2);

        assertTrue(ActivityFilters.filterByAvailability(availabilities, startingDate, endingDate));
    }
}
