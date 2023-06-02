package nl.tudelft.sem.template.activity.domain.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Availability {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * Setter for the date of the availability.
     *
     * @param attribute - Type of the date
     * @param value - Value of the date
     */
    public void timeSetter(String attribute, Date value) {
        switch (attribute) {
            case "startTime":
                this.startTime = value;
                break;
            case "endTime":
                this.endTime = value;
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "Availabilities{"
            + "startTime=" + startTime
            + ", endTime=" + endTime
            + '}';
    }
}
