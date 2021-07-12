package dwp.test;

import reactor.core.publisher.Flux;

public class TestUtils {

    private TestUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Flux<T> fluxOf(T... things) {
        return Flux.fromArray(things);
    }
}
