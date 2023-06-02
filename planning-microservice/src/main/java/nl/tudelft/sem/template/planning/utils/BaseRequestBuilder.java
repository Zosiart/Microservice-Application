package nl.tudelft.sem.template.planning.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public abstract class BaseRequestBuilder {

    protected final RestTemplate restTemplate;
    protected final String baseUrl;
    protected String auth;

    public BaseRequestBuilder(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Request using the authentication token.
     *
     * @param bearerToken - the auth token
     * @return the activity request builder
     */
    public BaseRequestBuilder withAuth(String bearerToken) {
        this.auth = bearerToken;

        return this;
    }

    /**
     * Builds the URI for the request.
     *
     * @return the URI
     */
    abstract String buildUri();

    /**
     * Send a request to the activity microservice.
     *
     * @param method - the http method
     * @return a response object
     */
    protected ResponseEntity<Object> send(HttpMethod method) {
        String uri = buildUri();

        System.out.println("Sending request to: " + uri);

        // authentication
        HttpEntity<?> entity = null;

        if (this.auth != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(auth);
            entity = new HttpEntity<>(headers);
        }

        return restTemplate.exchange(uri, method, entity, Object.class);
    }

    /**
     * Send a GET request to the activity microservice.
     *
     * @return a response object
     */
    public ResponseEntity<Object> get() {
        return send(HttpMethod.GET);
    }

    /**
     * Send a POST request to the activity microservice.
     *
     * @return a response object
     */
    public ResponseEntity<Object> post() {
        return send(HttpMethod.POST);
    }
}
