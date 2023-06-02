package nl.tudelft.sem.template.planning.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import nl.tudelft.sem.template.planning.domain.entity.Availability;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.enums.Certificate;
import nl.tudelft.sem.template.planning.domain.enums.Gender;
import nl.tudelft.sem.template.planning.domain.enums.Level;
import nl.tudelft.sem.template.planning.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

public class ActivityRequestBuilderTest {

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        restTemplate = mock(RestTemplate.class);
    }

    @Test
    public void testBuildUrl_withId() {
        ActivityRequestBuilder builder = new ActivityRequestBuilder(restTemplate, "http://localhost:8083/");
        String expectedUrl = "/123";

        String actualUrl = builder.withId(123).buildUri();

        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrl_withCertificate() {
        ActivityRequestBuilder builder = new ActivityRequestBuilder(restTemplate, "http://localhost:8083/");
        String expectedUrl = "?certificate=C_FOUR";

        String actualUrl = builder.withCertificate(Certificate.C_FOUR).buildUri();

        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrl_withRoles() {
        ActivityRequestBuilder builder = new ActivityRequestBuilder(restTemplate, "http://localhost:8083/");
        String expectedUrl =
            "?roles[0].role=Cox&roles[0].level=Amateur&roles[1].role="
                + "Coach&roles[1].level=Amateur&roles[2].role=Cox&roles[2].level=Amateur&roles[3].role="
                + "PortSideRower&roles[3].level=Amateur&roles[4].role=StarboardSideRower&roles[4].level="
                + "Amateur&roles[5].role=ScullingRower&roles[5].level=Amateur";

        List<RoleInfo> roles = Arrays.asList(new RoleInfo(Role.Cox, Level.Amateur), new RoleInfo(Role.Coach, Level.Amateur),
            new RoleInfo(Role.Cox, Level.Amateur),
            new RoleInfo(Role.PortSideRower, Level.Amateur), new RoleInfo(Role.StarboardSideRower, Level.Amateur),
            new RoleInfo(Role.ScullingRower, Level.Amateur));

        String actualUrl = builder.withRoles(roles).buildUri();

        System.out.println(actualUrl);

        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrl_withAvailabilities() throws ParseException {
        ActivityRequestBuilder builder = new ActivityRequestBuilder(restTemplate, "http://localhost:8083/");
        String expectedUrl =
            "?availabilities[0].startTime=2010-03-03 10:00:00&availabilities[0].endTime="
                + "2010-04-03 10:00:00&availabilities[1].startTime="
                + "2020-07-03 10:00:00&availabilities[1].endTime=2020-08-03 10:00:00";

        var formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Availability> availabilities =
            Arrays.asList(new Availability(formatter.parse("2010-03-03 10:00:00"), formatter.parse("2010-04-03 10:00:00")),
                new Availability(formatter.parse("2020-07-03 10:00:00"), formatter.parse("2020-08-03 10:00:00")));

        String actualUrl = builder.withAvailabilities(availabilities).buildUri();

        System.out.println(actualUrl);

        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrl_withOrganization() {
        ActivityRequestBuilder builder = new ActivityRequestBuilder(restTemplate, "http://localhost:8083/");
        String expectedUrl = "?organization=XYZ";

        String actualUrl = builder.withOrganization("XYZ").buildUri();

        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrl_withGender() {
        ActivityRequestBuilder builder = new ActivityRequestBuilder(restTemplate, "http://localhost:8083/");
        String expectedUrl = "?gender=MALE";

        String actualUrl = builder.withGender(Gender.MALE).buildUri();

        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrl_withOwner() {
        ActivityRequestBuilder builder = new ActivityRequestBuilder(restTemplate, "http://localhost:8083/");
        String expectedUrl = "?owner=123";

        String actualUrl = builder.withOwner(123).buildUri();

        assertTrue(actualUrl.endsWith(expectedUrl));
    }
}
