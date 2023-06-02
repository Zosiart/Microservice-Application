package nl.tudelft.sem.template.planning.profiles;

import nl.tudelft.sem.template.planning.utils.UserRequestBuilder;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("userRequestBuilder")
@Configuration
public class UserRequestBuilderProfile {

    @Bean
    @Primary
    public UserRequestBuilder userActivityRequestBuilder() {
        return Mockito.mock(UserRequestBuilder.class);
    }
}
