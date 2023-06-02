package nl.tudelft.sem.template.planning.profiles;


import nl.tudelft.sem.template.planning.application.client.UserManager;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("userManager")
@Configuration
public class UserManagerProfile {

    @Bean
    @Primary
    public UserManager getMockUserManager() {
        return Mockito.mock(UserManager.class);
    }
}
