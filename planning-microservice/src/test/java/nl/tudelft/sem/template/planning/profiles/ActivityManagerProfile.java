package nl.tudelft.sem.template.planning.profiles;

import nl.tudelft.sem.template.planning.application.client.ActivityManager;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("activityManager")
@Configuration
public class ActivityManagerProfile {

    @Bean
    @Primary
    public ActivityManager getMockActivityManager() {
        return Mockito.mock(ActivityManager.class);
    }
}
