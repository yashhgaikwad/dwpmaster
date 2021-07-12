package dwp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;

import dwp.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DistanceCalculatorTest {

    static final Location LONDON = new Location(51.507222D, -0.1275D);
    static final Location BRISBANE = new Location(-27.467778D, 153.028056D);

    @InjectMocks
    private DistanceCalculator distanceCalculator;

    @Test
    void shouldCalculateMilesBetween2Points() {
        // https://www.freemaptools.com/how-far-is-it-between-london_-uk-and-brisbane_-australia.htm
        assertThat(distanceCalculator.milesBetween(LONDON, BRISBANE))
            .isCloseTo(10268.0D, byLessThan(1.0D));
    }

    @Test
    void shouldGivePositiveDistanceIfPointsReversed() {
        assertThat(distanceCalculator.milesBetween(BRISBANE, LONDON))
            .isCloseTo(10268.0D, byLessThan(1.0D));
    }
}
