package nl.tudelft.sem.template.authentication.config;

import lombok.Getter;
import lombok.Setter;
import nl.tudelft.sem.template.authentication.domain.user.service.PasswordHashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * The type Web security config.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Getter
    @Setter(onMethod = @__({@Autowired})) // add autowired annotation on setter
    private transient UserDetailsService userDetailsService;

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sha512PasswordEncoder();
    }

    @Bean
    public PasswordHashingService passwordHashEncoder() {
        return new PasswordHashingService(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/h2-console/**").permitAll()
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
