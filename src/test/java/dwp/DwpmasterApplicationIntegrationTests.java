package dwp;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
class DwpmasterApplicationIntegrationTests {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeAll
    public void setup() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @Test
    void shouldPublishSwaggerApiDescription() {
        webTestClient.get()
            .uri("/v3/api-docs")
            .exchange()
            .expectHeader().valueEquals(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    }

    @Test
    void shouldPublishSwaggerUi() {
        webTestClient.get()
            .uri("/swagger-ui/")
            .exchange()
            .expectHeader().valueEquals(CONTENT_TYPE, TEXT_HTML_VALUE)
            .expectBody();
    }
}
