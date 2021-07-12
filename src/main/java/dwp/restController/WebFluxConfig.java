package dwp.restController;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.ServerCodecConfigurer.ServerDefaultCodecs;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        addSwaggerResources(registry);
    }

    private void addSwaggerResources(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
            .resourceChain(false);
    }

    /**
     * Webflux to use custom Jackson object mapper.
     */
    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configureObjectMapper(configurer.defaultCodecs());
    }

    private void configureObjectMapper(ServerDefaultCodecs codecs) {
        ObjectMapper objectMapper = objectMapper();
        codecs.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
        codecs.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
    }
    
    private ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder()
            .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .build();
    }
}
