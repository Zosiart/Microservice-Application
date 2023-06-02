package nl.tudelft.sem.template.planning.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import nl.tudelft.sem.template.planning.domain.entity.Availability;
import nl.tudelft.sem.template.planning.domain.entity.RoleInfo;
import nl.tudelft.sem.template.planning.domain.enums.Certificate;
import nl.tudelft.sem.template.planning.domain.enums.Gender;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ActivityRequestBuilder extends BaseRequestBuilder {

    private String route;
    private Long id;
    private Certificate certificate;
    private List<RoleInfo> roles;
    private List<Availability> availabilities;
    private String organization;
    private Gender gender;
    private Long userId; //owner

    public ActivityRequestBuilder(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate, baseUrl);
    }

    /**
     * Request using a specific route.
     *
     * @param route - the route
     * @return the activity request builder
     */
    public ActivityRequestBuilder withRoute(String route) {
        this.route = route;

        return this;
    }

    /**
     * Request using activity id.
     *
     * @param id - the activity id
     * @return the activity request builder
     */
    public ActivityRequestBuilder withId(long id) {
        this.id = id;

        return this;
    }

    /**
     * Request using the owner id.
     *
     * @param userId - owner id
     * @return the activity request builder
     */
    public ActivityRequestBuilder withOwner(long userId) {
        this.userId = userId;

        return this;
    }

    /**
     * Request using certificate.
     *
     * @param certificate - certificate
     * @return the activity request builder
     */
    public ActivityRequestBuilder withCertificate(Certificate certificate) {
        this.certificate = certificate;

        return this;
    }

    /**
     * Request using roles.
     *
     * @param roles - roles
     * @return the activity request builder
     */
    public ActivityRequestBuilder withRoles(List<RoleInfo> roles) {
        this.roles = roles;

        return this;
    }

    /**
     * Request using organization.
     *
     * @param organization - organization
     * @return the activity request builder
     */
    public ActivityRequestBuilder withOrganization(String organization) {
        this.organization = organization;

        return this;
    }

    /**
     * Request using gender.
     *
     * @param gender - gender
     * @return the activity request builder
     */
    public ActivityRequestBuilder withGender(Gender gender) {
        this.gender = gender;

        return this;
    }

    /**
     * Request using list of availabilities.
     *
     * @param availabilities - List of Availability
     * @return the activity request builder
     */
    public ActivityRequestBuilder withAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;

        return this;
    }

    private String encodeRoles() {
        return IntStream.range(0, roles.size()).mapToObj(i -> {
            var role = roles.get(i);
            return String.format("roles[%d].role=%s&roles[%d].level=%s", i, role.getRole(), i, role.getLevel());
        }).collect(Collectors.joining("&"));

    }

    private String encodeAvailabilities() {
        return IntStream.range(0, availabilities.size()).mapToObj(i -> {
            var availability = availabilities.get(i);

            var mapper = new ObjectMapper();

            var formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            mapper.setDateFormat(formatter);

            String start = formatter.format(availability.getStartTime());
            String end = formatter.format(availability.getEndTime());

            return String.format("availabilities[%d].startTime=%s&availabilities[%d].endTime=%s", i, start, i, end);

        }).collect(Collectors.joining("&"));

    }

    /**
     * Builds the URI for the request.
     *
     * @return the URI
     */
    public String buildUri() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        if (this.route != null) {
            builder.pathSegment(this.route);
        }

        if (this.id != null) {
            builder.pathSegment(this.id.toString());
        }

        if (this.certificate != null) {
            builder.queryParam("certificate", this.certificate);
        }

        if (this.roles != null) {
            builder.queryParam(encodeRoles());
        }

        if (this.organization != null) {
            builder.queryParam("organization", this.organization);
        }

        if (this.gender != null) {
            builder.queryParam("gender", this.gender);
        }

        if (this.userId != null) {
            builder.queryParam("owner", this.userId);
        }

        if (this.availabilities != null) {
            builder.queryParam(encodeAvailabilities());
        }

        return builder.build().toUriString();
    }

}
