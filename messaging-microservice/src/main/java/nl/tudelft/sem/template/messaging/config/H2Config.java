package nl.tudelft.sem.template.messaging.config;

import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * The H2 config.
 */
@Configuration
@EnableJpaRepositories("nl.tudelft.sem.template.messaging.domain")
@PropertySource("classpath:application-dev.properties")
@EnableTransactionManagement
public class H2Config {

    @Getter
    private final Environment environment;

    public H2Config(Environment environment) {
        this.environment = environment;
    }

    /**
     * Set up the connection to the database.
     *
     * @return The data source.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("jdbc.driverClassName")));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.user"));
        dataSource.setPassword(environment.getProperty("jdbc.pass"));

        return dataSource;
    }

    /**
     * Set up java mail sender.
     *
     * @return the java mail sender
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        //TODO pick up new account
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.poczta.onet.pl");
        mailSender.setPort(465);
        mailSender.setUsername("sem30a@onet.pl");
        mailSender.setPassword("Alamakota13@");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
