package net.remgant.weather;

import net.remgant.weather.dao.WeatherUpdateRepository;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class UpdaterApplication {

    final static private Logger log = LoggerFactory.getLogger(UpdaterApplication.class);
    private final Clock clock = Clock.systemUTC();
    final WeatherUpdateRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(UpdaterApplication.class, args);
    }

    public UpdaterApplication(WeatherUpdateRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody WeatherUpdate data) {
        if (data.getTimestamp() == null)
            data.setTimestamp(Instant.now(clock));
        log.info("Got {}", data);
        repository.save(data);
    }

    @RequestMapping("/user")
    public Map<String, Object> user(Principal principal) {
        return Map.of("text", String.format("Hello User %s!", principal.getName()));
    }


    //TODO added for testing, remove it since it's too dangerous to have in final product
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void deleteAll() {
        repository.deleteAll();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
