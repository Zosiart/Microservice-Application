
package nl.tudelft.sem.template.planning.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Availability {

    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Override
    public String toString() {
        return "Availabilities{"
            + "startTime=" + startTime
            + ", endTime=" + endTime
            + '}';
    }
}
