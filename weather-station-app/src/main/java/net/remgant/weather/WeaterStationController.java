package net.remgant.weather;

import net.remgant.weather.dao.WeatherUpdateRepository;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
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

    public WeaterStationController(WeatherUpdateRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody WeatherUpdate data) {
        log.info("Got {}", data);
        repository.save(data);
    }

    final static private String MAX_INSTANT_VALUE = Long.toString(Instant.parse("9999-12-31T23:59:59.999Z").getEpochSecond());
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Collection<WeatherUpdate> data(@RequestParam(value = "start") Optional<String> start,
                                          @RequestParam(value = "end") Optional<String> end) {
        Instant s = Instant.ofEpochSecond(Long.parseLong(start.orElse("0")));
        Instant e = Instant.ofEpochSecond(Long.parseLong(end.orElse(MAX_INSTANT_VALUE)));
        return repository.findAll(getWeatherUpdateSpecification(s,e));
    }

    private Specification<WeatherUpdate> getWeatherUpdateSpecification(Instant start, Instant end) {
            return (Specification<WeatherUpdate>) (root, query, builder) -> {
                final List<Predicate> predicates = new ArrayList<>();
                predicates.add(builder.greaterThanOrEqualTo(root.get("timestamp"), start));
                predicates.add(builder.lessThanOrEqualTo(root.get("timestamp"), end));
                return builder.and(predicates.toArray(new Predicate[0]));
            };
        }
}
