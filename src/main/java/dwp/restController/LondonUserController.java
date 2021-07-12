package dwp.restController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import dwp.model.User;
import dwp.service.LondonUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

/**
 * @author Amit
 * @apiNote you can get londonuser from LondonUserController
 *
 */

@RestController
public class LondonUserController {

    @Autowired
    private LondonUserService londonUserService;

    private static Logger logger = LoggerFactory.getLogger(LondonUserController.class);

    @GetMapping(path = "/londonusers", produces = { APPLICATION_JSON_VALUE })
    ResponseEntity<Flux<User>> londonUsers() {
        logger.info("Request received to all londonusers");
        return ok().body(londonUserService.londonUsers());
    }
}
