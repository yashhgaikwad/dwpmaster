package dwp.service;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import dwp.model.User;

class BpdtsClientTest {

    private WireMockServer wireMockServer;

    private BpdtsClient bpdtsClient;

    @BeforeEach
    private void setup() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
        wireMockServer.start();

        bpdtsClient = new BpdtsClient("http://localhost:" + wireMockServer.port());
    }

    @AfterEach
    private void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldGetUsersByCity() {
        wireMockServer.stubFor(
            WireMock.get("/city/London/users")
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                    .withBody("[{"
                        + "\"id\": 7,"
                        + "\"first_name\": \"James\","
                        + "\"last_name\": \"Bond\","
                        + "\"email\": \"james.bond@sis.gov.uk\","
                        + "\"ip_address\": \"104.18.6.144\","
                        + "\"latitude\": 51.487222,"
                        + "\"longitude\": -0.124167"
                        + "}]")));

        Flux<User> users = bpdtsClient.usersByCity("London");

        StepVerifier.create(users)
            .expectNext(User.builder()
                .id(7L)
                .firstName("James")
                .lastName("Bond")
                .email("james.bond@sis.gov.uk")
                .ipAddress("104.18.6.144")
                .latitude(51.487222D)
                .longitude(-0.124167D)
                .build())
            .expectComplete()
            .verify();
    }
    
    @Test
    void shouldGetAllUsers() {
        wireMockServer.stubFor(
            WireMock.get("/users")
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                    .withBody("[{"
                        + "\"id\": 7,"
                        + "\"first_name\": \"James\","
                        + "\"last_name\": \"Bond\","
                        + "\"email\": \"james.bond@sis.gov.uk\","
                        + "\"ip_address\": \"104.18.6.144\","
                        + "\"latitude\": 51.487222,"
                        + "\"longitude\": -0.124167"
                        + "}]")));

        Flux<User> users = bpdtsClient.allUsers();

        StepVerifier.create(users)
            .expectNext(User.builder()
                .id(7L)
                .firstName("James")
                .lastName("Bond")
                .email("james.bond@sis.gov.uk")
                .ipAddress("104.18.6.144")
                .latitude(51.487222D)
                .longitude(-0.124167D)
                .build())
            .expectComplete()
            .verify();
    }

    @Test
    void shouldFailWhenUsersFailValidation() {
        wireMockServer.stubFor(
            WireMock.get("/users")
                .willReturn(WireMock.aResponse()
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                    .withBody("[{},{}]")));

        Flux<User> users = bpdtsClient.allUsers();

        StepVerifier.create(users)
            .expectError(ConstraintViolationException.class)
            .verify();
    }
}