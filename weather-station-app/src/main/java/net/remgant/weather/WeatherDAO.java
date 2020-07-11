package net.remgant.weather;

import java.time.Instant;
import java.util.Collection;

public interface WeatherDAO {

    void update(WeatherUpdate weatherUpdate);

    default Collection<WeatherUpdate> findUpdatesSince(Instant instant) {
        return findUpdatesBetween(instant, Instant.now());
    }

    Collection<WeatherUpdate> findUpdatesBetween(Instant start, Instant end);
}
