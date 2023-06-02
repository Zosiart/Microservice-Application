package nl.tudelft.sem.template.planning.profiles;

import nl.tudelft.sem.template.planning.domain.service.ActivityUtils;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("activityUtils")
@Configuration
public class ActivityUtilsProfile {

    @Bean
    @Primary
    public ActivityUtils getMockActivityUtils() {
        return Mockito.mock(ActivityUtils.class);
    }
}
