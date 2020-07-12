package net.remgant.weather;

import net.remgant.weather.dao.WeatherUpdateRepository;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WeaterStationController {
    private final static Logger log = LoggerFactory.getLogger(WeaterStationController.class);

    final WeatherUpdateRepository repository;
    private Clock clock;

    public WeaterStationController(WeatherUpdateRepository repository) {
        this.repository = repository;
        this.clock = Clock.systemUTC();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody WeatherUpdate data) {
        data.setTimestamp(Instant.now(clock));
        log.info("Got {}", data);
        repository.save(data);
    }

    final static private String MAX_INSTANT_VALUE = Long.toString(Instant.parse("9999-12-31T23:59:59.999Z").getEpochSecond());

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Collection<WeatherUpdate> data(@RequestParam(value = "start") Optional<String> start,
                                          @RequestParam(value = "end") Optional<String> end,
                                          @RequestParam(value = "page") Optional<Integer> page,
                                          @RequestParam(value = "size") Optional<Integer> size) {
        Instant s = Instant.ofEpochSecond(Long.parseLong(start.orElse("0")));
        Instant e = Instant.ofEpochSecond(Long.parseLong(end.orElse(MAX_INSTANT_VALUE)));
        return repository.findAll(getWeatherUpdateSpecification(s, e), PageRequest.of(page.orElse(0), size.orElse(60),
                Sort.Direction.DESC, "timestamp")).toList();
    }

    private Specification<WeatherUpdate> getWeatherUpdateSpecification(Instant start, Instant end) {
        return (Specification<WeatherUpdate>) (root, query, builder) ->
                builder.and(builder.greaterThanOrEqualTo(root.get("timestamp"), start),
                builder.lessThanOrEqualTo(root.get("timestamp"),end));
    }
}
