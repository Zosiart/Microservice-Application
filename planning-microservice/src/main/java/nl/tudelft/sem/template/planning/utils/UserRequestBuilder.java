package nl.tudelft.sem.template.planning.utils;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UserRequestBuilder extends BaseRequestBuilder {

    private String route;
    private Long id;

    public UserRequestBuilder(RestTemplate restTemplate, String baseUrl) {
        super(restTemplate, baseUrl);
    }

    /**
     * Request using a specific route.
     *
     * @param route - the route
     * @return the user request builder
     */
    public UserRequestBuilder withRoute(String route) {
        this.route = route;

        return this;
    }

    /**
     * Request using user id.
     *
     * @param id - the user id
     * @return the user request builder
     */
    public UserRequestBuilder withId(long id) {
        this.id = id;

        return this;
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

        return builder.build().toUriString();
    }

}
