package nl.tudelft.sem.template.planning.profiles;

import nl.tudelft.sem.template.planning.utils.ActivityRequestBuilder;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("activityRequestBuilder")
@Configuration
public class ActivityRequestBuilderProfile {

    @Bean
    @Primary
    public ActivityRequestBuilder getActivityRequestBuilder() {
        return Mockito.mock(ActivityRequestBuilder.class);
    }
}
