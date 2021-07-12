package dwp.service;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import dwp.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.ClientCodecConfigurer.ClientDefaultCodecs;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import reactor.core.publisher.Flux;

/**
 * Streaming client used to interact with the bpdts-test-app.herokuapp.com rest
 * services
 */
@Component
public class BpdtsClient {

    private final WebClient webClient;

    private final Validator validator;

    public BpdtsClient(@Value("${bpdts.url}") String bpdtsUrl) {
        webClient = bpdtsWebClient(bpdtsUrl);
        validator = validator();
    }

    public Flux<User> usersByCity(String city) {
        return webClient.get()
            .uri("/city/{city}/users", city)
            .accept(APPLICATION_STREAM_JSON, APPLICATION_JSON)
            .exchange()
            .flatMapMany(response -> response.bodyToFlux(User.class))
            .doOnNext(this::validate);
    }

    public Flux<User> allUsers() {
        return webClient.get()
            .uri("/users")
            .accept(APPLICATION_STREAM_JSON, APPLICATION_JSON)
            .exchange()
            .flatMapMany(response -> response.bodyToFlux(User.class))
            .doOnNext(this::validate);
    }

    private WebClient bpdtsWebClient(String baseUrl) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .exchangeStrategies(snakeCaseExchangeStrategies())
            .build();
    }

    private ExchangeStrategies snakeCaseExchangeStrategies() {
        return ExchangeStrategies.builder()
            .codecs(codecsConfigurer -> configureCodecs(codecsConfigurer))
            .build();
    }

    private void configureCodecs(ClientCodecConfigurer codecsConfigurer) {
        ObjectMapper mapper = snakeCaseObjectMapper();
        ClientDefaultCodecs codecs = codecsConfigurer.defaultCodecs();

        codecs.jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
        codecs.jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
    }

    private ObjectMapper snakeCaseObjectMapper() {
        return new Jackson2ObjectMapperBuilder()
            .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .build();
    }

    private Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    private <T> void validate(T pojo) {
        Set<ConstraintViolation<T>> violations = validator.validate(pojo);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
