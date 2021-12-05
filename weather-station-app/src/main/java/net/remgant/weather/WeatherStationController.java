package net.remgant.weather;

import net.remgant.weather.dao.WeatherUpdateRepository;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WeatherStationController {
    private final static Logger log = LoggerFactory.getLogger(WeatherStationController.class);

    final WeatherUpdateRepository repository;
    final WeatherEventUpdateService weatherEventUpdateService;
    final private Clock clock;

    public WeatherStationController(WeatherUpdateRepository repository, WeatherEventUpdateService weatherEventUpdateService) {
        this.repository = repository;
        this.weatherEventUpdateService = weatherEventUpdateService;
        this.clock = Clock.systemUTC();
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public List<WeatherUpdate> data(@RequestParam(value = "start", defaultValue = "0") long start,
                                    @RequestParam(value = "end", defaultValue = "253402300799") long end,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "60") int size) {
        return repository.findAll((root, query, builder) ->
                        builder.and(builder.greaterThanOrEqualTo(root.get("timestamp"), Instant.ofEpochSecond(start)),
                                builder.lessThanOrEqualTo(root.get("timestamp"), Instant.ofEpochSecond(end))),
                PageRequest.of(page, size, Sort.Direction.DESC, "timestamp")).toList();
    }
}
