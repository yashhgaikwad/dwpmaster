package dwp.service;

import static org.mockito.BDDMockito.given;
import static dwp.test.TestUtils.fluxOf;

import dwp.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import dwp.model.User;

@ExtendWith(MockitoExtension.class)
class LondonUserServiceTest {

    static final Location LONDON = new Location(51.507222D, -0.1275D);
    static final Location LONDON_PITTSBURG = new Location(37.13D, -84.08D);
    static final Location EPPING_FOREST = new Location(51.6553959D, 0.0572553D);
    static final Location VAUXHALL_BRIDGE = new Location(51.487222D, -0.124167D);
    static final Location JAKARTA = new Location(-6.5115909D, 105.652983D);
    static final Location GLASGOW = new Location(55.860916, -4.251433);

    static final User LIVES_IN_LONDON_CURRENTLY_IN_JAKARTA = User.builder()
        .id(135L)
        .firstName("Mechelle")
        .lastName("Boam")
        .email("mboam3q@thetimes.co.uk")
        .ipAddress("113.71.242.187")
        .latitude(JAKARTA.getLatitude())
        .longitude(JAKARTA.getLongitude())
        .build();

    static final User LIVES_AND_CURRENTLY_IN_LONDON_PITTSBURG = User.builder()
        .id(688L)
        .firstName("Tiffi")
        .lastName("Colbertson")
        .email("tcolbertsonj3@vimeo.com")
        .ipAddress("141.49.93.0")
        .latitude(LONDON_PITTSBURG.getLatitude())
        .longitude(LONDON_PITTSBURG.getLongitude())
        .build();

    static final User LIVES_IN_RUSSIA_CURRENTLY_NEAR_LONDON = User.builder()
        .id(266L)
        .firstName("Ancell")
        .lastName("Garnsworthy")
        .email("agarnsworthy7d@seattletimes.com")
        .ipAddress("67.4.69.137")
        .latitude(EPPING_FOREST.getLatitude())
        .longitude(EPPING_FOREST.getLongitude())
        .build();

    static final User LIVES_IN_LONDON_CURRENTLY_IN_LONDON = User.builder()
        .id(7L)
        .firstName("James")
        .lastName("Bond")
        .email("james.bond@sis.gov.uk")
        .ipAddress("104.18.6.144")
        .latitude(VAUXHALL_BRIDGE.getLatitude())
        .longitude(VAUXHALL_BRIDGE.getLongitude())
        .build();

    static final User LIVES_AND_CURRENTLY_IN_GLASGOW = User.builder()
        .id(8L)
        .firstName("George")
        .lastName("MacLeod")
        .email("gmac@loud.com")
        .ipAddress("105.197.221.1")
        .latitude(GLASGOW.getLatitude())
        .longitude(GLASGOW.getLongitude())
        .build();

    @Mock
    private BpdtsClient bpdtsClient;

    @Mock
    private DistanceCalculator distanceCalculator;

    @InjectMocks
    private LondonUserService londonUserService;

    @Test
    void shouldCombineUsersLivingInAnyLondon_WithThoseCurrentlyWithin50MilesLondonUk() {
        given(bpdtsClient.usersByCity("London"))
            .willReturn(fluxOf(
                LIVES_IN_LONDON_CURRENTLY_IN_JAKARTA,
                LIVES_AND_CURRENTLY_IN_LONDON_PITTSBURG));

        given(bpdtsClient.allUsers())
            .willReturn(fluxOf(
                LIVES_IN_RUSSIA_CURRENTLY_NEAR_LONDON));

        given(distanceCalculator.milesBetween(LONDON, EPPING_FOREST))
            .willReturn(12.071D);

        Flux<User> users = londonUserService.londonUsers();

        StepVerifier.create(users)
            .expectNext(LIVES_IN_LONDON_CURRENTLY_IN_JAKARTA)
            .expectNext(LIVES_AND_CURRENTLY_IN_LONDON_PITTSBURG)
            .expectNext(LIVES_IN_RUSSIA_CURRENTLY_NEAR_LONDON)
            .expectComplete()
            .verify();
    }

    @Test
    void shouldRemoveUsersNotLivingInLondonUk_andCurrentlyOutside50Miles() {
        given(bpdtsClient.usersByCity("London"))
            .willReturn(fluxOf());

        given(bpdtsClient.allUsers())
            .willReturn(fluxOf(
                LIVES_AND_CURRENTLY_IN_GLASGOW,
                LIVES_IN_RUSSIA_CURRENTLY_NEAR_LONDON));

        given(distanceCalculator.milesBetween(LONDON, GLASGOW))
            .willReturn(345.606D);
        given(distanceCalculator.milesBetween(LONDON, EPPING_FOREST))
            .willReturn(12.071D);

        Flux<User> users = londonUserService.londonUsers();

        StepVerifier.create(users)
            .expectNext(LIVES_IN_RUSSIA_CURRENTLY_NEAR_LONDON)
            .expectComplete()
            .verify();
    }

    @Test
    void shouldRemoveDuplicates_ofUsersBothLivingAndCurrentlyInLondonUk() {
        given(bpdtsClient.usersByCity("London"))
            .willReturn(fluxOf(
                LIVES_IN_LONDON_CURRENTLY_IN_LONDON));

        given(bpdtsClient.allUsers())
            .willReturn(fluxOf(
                LIVES_IN_LONDON_CURRENTLY_IN_LONDON));

        given(distanceCalculator.milesBetween(LONDON, VAUXHALL_BRIDGE))
            .willReturn(1.4D);

        Flux<User> users = londonUserService.londonUsers();

        StepVerifier.create(users)
            .expectNext(LIVES_IN_LONDON_CURRENTLY_IN_LONDON)
            .expectComplete()
            .verify();
    }
}
