package nl.tudelft.sem.template.activity.domain.activity;

import java.util.Date;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity(name = "Training")
@DiscriminatorValue("Training")
@NoArgsConstructor
public class Training extends Activity {

    /**
     * Constructor of Training.
     *
     * @param userId of the author
     * @param startingDate of the training
     * @param endingDate of the training
     * @param description of the training
     * @param certificate needed for the cox if applicable
     * @param roles necessary for this training
     */
    public Training(Integer userId, Date startingDate, Date endingDate, String description,
                    Certificate certificate, List<RoleAvailability> roles) {
        super(userId, startingDate, endingDate, description, certificate, roles);

    }
}
