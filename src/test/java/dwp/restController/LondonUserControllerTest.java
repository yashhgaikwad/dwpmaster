package dwp.restController;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dwp.model.User;
import dwp.service.LondonUserService;
import dwp.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
class LondonUserControllerTest {

    @MockBean
    private LondonUserService londonUserService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnUsersFromService() {
        given(londonUserService.londonUsers())
            .willReturn(TestUtils.fluxOf(User.builder()
                .id(007L)
                .firstName("James")
                .lastName("Bond")
                .email("james.bond@sis.gov.uk")
                .ipAddress("104.18.6.144")
                .latitude(51.487222)
                .longitude(-0.124167)
                .build()));

        webTestClient.get()
            .uri("/londonusers")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectHeader().valueEquals(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .expectBody().json("[{"
                + "'id':7,"
                + "'first_name':'James',"
                + "'last_name':'Bond',"
                + "'email':'james.bond@sis.gov.uk',"
                + "'ip_address':'104.18.6.144',"
                + "'latitude':51.487222,"
                + "'longitude':-0.124167"
                + "}]");
    }
}
