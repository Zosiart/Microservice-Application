package nl.tudelft.sem.template.planning.utils;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

public class UserRequestBuilderTest {

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        restTemplate = mock(RestTemplate.class);
    }

    @Test
    public void testBuildUrl_withId() {
        UserRequestBuilder builder = new UserRequestBuilder(restTemplate, "http://localhost:8082/");
        String expectedUrl = "/123";

        String actualUrl = builder.withId(123).buildUri();

        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrlWithRoute() {
        UserRequestBuilder builder = new UserRequestBuilder(restTemplate, "http://localhost:8082");
        String expectedUrl = "abc";

        String actualUrl = builder.withRoute("abc").buildUri();
        System.out.println(actualUrl);
        assertTrue(actualUrl.endsWith(expectedUrl));
    }

    @Test
    public void testBuildUrlWithBoth() {
        UserRequestBuilder builder = new UserRequestBuilder(restTemplate, "http://localhost:8082");
        String expectedUrl = "abc/999";

        String actualUrl = builder.withRoute("abc").withId(999).buildUri();
        System.out.println(actualUrl);
        assertTrue(actualUrl.endsWith(expectedUrl));
    }

}
