package nl.tudelft.sem.template.planning.profiles;

import nl.tudelft.sem.template.planning.application.planner.RegisterForActivityListener;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("registerForActivityListener")
@Configuration
public class RegisterForActivityListenerProfile {

    @Bean
    @Primary
    public RegisterForActivityListener getRegisterForActivityListener() {
        return Mockito.mock(RegisterForActivityListener.class);
    }
}
