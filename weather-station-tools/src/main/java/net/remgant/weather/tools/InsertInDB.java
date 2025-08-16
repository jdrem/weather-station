package net.remgant.weather.tools;

import net.remgant.weather.dao.WeatherUpdateRepository;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component("InsertInDB")
public class InsertInDB implements CommandLineApplication {
    final private static Logger log = LoggerFactory.getLogger(InsertInDB.class);
    final private WeatherUpdateRepository weatherUpdateRepository;


    public InsertInDB(WeatherUpdateRepository weatherUpdateRepository) {
        this.weatherUpdateRepository = weatherUpdateRepository;
    }

    final double minTemp = 70.0;
    final double maxTemp = 89.0;
    final double tempAmplitude = (maxTemp - minTemp) / 2.0;
    final double tempOffset = tempAmplitude / 2.0 + minTemp;
    final double maxHumidity = 85.0;
    final double minHumidity = 65.0;
    final double humidityAmplitude = (maxHumidity - minHumidity) / 2.0;
    final double humidityOffset = humidityAmplitude / 2.0 + minHumidity;
    long zoneOffset;
    final static double SECONDS_IN_DAY = 24.0 * 60.0 * 60.0;

    @Override
    public void run(String... args) throws Exception {
        Instant start = Instant.now().minus(1L, ChronoUnit.HOURS);
        zoneOffset = ZoneId.of("America/New_York").getRules().getOffset(start).getTotalSeconds();

        Instant now = Instant.now();
        Duration interval = Duration.of(1L, ChronoUnit.MINUTES);

        while (start.isBefore(now)) {
            WeatherUpdate weatherUpdate = createWeatherUpdateAtTime(start);
            log.info("Updating: {}", weatherUpdate);
            weatherUpdateRepository.save(weatherUpdate);
            start = start.plus(interval.toMillis(), ChronoUnit.MILLIS);
        }
    }

    private WeatherUpdate createWeatherUpdateAtTime(Instant time) {
        double t = ((double) (time.getEpochSecond() + zoneOffset) % SECONDS_IN_DAY);
        double tempF = tempAmplitude * Math.sin(t / SECONDS_IN_DAY * 2.0 * Math.PI) + tempOffset;
        double tempC = (tempF - 32.0) * (5.0 / 9.0);
        double humidity = humidityAmplitude * Math.sin(t / SECONDS_IN_DAY * 2.0 * Math.PI + Math.PI) + humidityOffset;
        double pressure = 101592.0;
        return new WeatherUpdate(time, tempF, tempC, humidity, pressure);
    }
}
