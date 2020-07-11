package net.remgant.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WeaterStationController {
    private final static Logger log = LoggerFactory.getLogger(WeaterStationController.class);
    private WeatherDAO weatherDAO;

    public WeaterStationController(WeatherDAO weatherDAO) {
        this.weatherDAO = weatherDAO;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody WeatherUpdate data) {
        log.info("Got {}", data);
        weatherDAO.update(data);
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Collection<WeatherUpdate> data(@RequestParam(value = "start") Optional<String> start,
                                          @RequestParam(value = "end") Optional<String> end) {
        return weatherDAO.findUpdatesBetween((new Date(Long.parseLong(start.orElse("0")))).toInstant(),
                (new Date(Long.parseLong(end.orElse("1000000")))).toInstant());

    }
}
