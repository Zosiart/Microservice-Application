package nl.tudelft.sem.template.authentication.domain.user.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Availability {

    private Date startTime;
    private Date endTime;

    @Override
    public String toString() {
        return "Availabilities{"
            + "startTime=" + startTime
            + ", endTime=" + endTime
            + '}';
    }
}
