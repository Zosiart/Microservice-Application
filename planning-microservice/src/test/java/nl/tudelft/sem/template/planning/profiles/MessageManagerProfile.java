package nl.tudelft.sem.template.planning.profiles;

import nl.tudelft.sem.template.planning.application.client.MessageManager;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("messageManager")
@Configuration
public class MessageManagerProfile {

    @Bean
    @Primary  // marks this bean as the first bean to use when trying to inject an AuthenticationManager
    public MessageManager getMockMessageManager() {
        return Mockito.mock(MessageManager.class);
    }
}
