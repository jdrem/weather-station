package net.remgant.weather.tools;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import net.remgant.weather.dao.WeatherUpdateRepository;
import net.remgant.weather.model.WeatherUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component("FetchDataFromNWS")
public class FetchDataFromNWS implements CommandLineApplication {
    final private static Logger log = LoggerFactory.getLogger(InsertInDB.class);
    final private WeatherUpdateRepository weatherUpdateRepository;

    public FetchDataFromNWS(WeatherUpdateRepository weatherUpdateRepository) {
        this.weatherUpdateRepository = weatherUpdateRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // https://api.weather.gov//stations/KFIT/observations/current
        NWSClient nwsClient;

        nwsClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(NWSClient.class, "https://api.weather.gov");
        Observation latestObservations = nwsClient.getAllAvailableObservations("KFIT");
        System.out.println(latestObservations);
        for (Observation.Features f : latestObservations.getFeatures()) {
            log.info("processing {}", f);
            // 2025-08-05T23:25:00+00:00
            OffsetDateTime offsetDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(f.properties.getTimestamp(), OffsetDateTime::from);
            ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("UTC"));
            double tempC = f.getProperties().getTemperature().getValue();
            double tempF = tempC * 9.0 / 5.0 + 32;
            double humidity = f.getProperties().getRelativeHumidity().getValue();
            double pressure = f.getProperties().getBarometricPressure().getValue();
            WeatherUpdate weatherUpdate = new  WeatherUpdate(zonedDateTime.toInstant(), tempF, tempC, humidity, pressure);
            weatherUpdateRepository.save(weatherUpdate);
        }

    }
}
