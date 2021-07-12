package dwp.restController;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class RestApiConfig {

    /**
     * Sets up the publishing of the REST API on http://localhost:8080/swagger-ui/
     */
    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
            .select()
            .apis(basePackage(thisPackage()))
            .paths(any())
            .build();
    }

    private String thisPackage() {
        return this.getClass().getPackage().getName();
    }
}
