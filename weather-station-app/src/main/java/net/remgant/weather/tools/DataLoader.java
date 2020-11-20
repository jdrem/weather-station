package net.remgant.weather.tools;

import net.remgant.weather.model.WeatherUpdate;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class DataLoader {

    public static void main(String args[]) {
        new DataLoader().run();
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

    public void run() {

        Instant start = Instant.now().minus(1L, ChronoUnit.DAYS);
        zoneOffset = ZoneId.of("America/New_York").getRules().getOffset(start).getTotalSeconds();

        Instant now = Instant.now();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String auth = "admin" + ":" + "password";
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        URI uri;
        try {
            uri = new URI("http://localhost:9099/api/deleteAll");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        RequestEntity<Void> deleteAllRequest = RequestEntity.get(uri)
                .headers(headers)
                .build();
        restTemplate.exchange(deleteAllRequest, Void.class);

        try {
            uri = new URI("http://localhost:9099/api/update");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Duration interval = Duration.of(2L, ChronoUnit.HOURS);

        while (start.isBefore(now)) {
            WeatherUpdate weatherUpdate = createWeatherUpdateAtTime(start);
            System.out.println(weatherUpdate);

            RequestEntity<WeatherUpdate> weatherUpdateRequest = RequestEntity.post(uri)
                    .headers(headers)
                    .body(weatherUpdate);
            restTemplate.exchange(weatherUpdateRequest, Void.class);
            start = start.plus(interval.toMillis(), ChronoUnit.MILLIS);
        }

        while (true) {
            try {
                Thread.sleep(interval.toMillis());
            } catch (InterruptedException e) {

            }
            WeatherUpdate weatherUpdate = createWeatherUpdateAtTime(Instant.now());
            System.out.println(weatherUpdate);
            RequestEntity<WeatherUpdate> weatherUpdateRequest = RequestEntity.post(uri)
                    .headers(headers)
                    .body(weatherUpdate);
            restTemplate.exchange(weatherUpdateRequest, Void.class);
        }

    }

    WeatherUpdate createWeatherUpdateAtTime(Instant time) {
        double t = ((double) (time.getEpochSecond() + zoneOffset) % SECONDS_IN_DAY);
        double tempF = tempAmplitude * Math.sin(t / SECONDS_IN_DAY * 2.0 * Math.PI) + tempOffset;
        double tempC = (tempF - 32.0) * (5.0 / 9.0);
        double humidity = humidityAmplitude * Math.sin(t / SECONDS_IN_DAY * 2.0 * Math.PI + Math.PI) + humidityOffset;
        return new WeatherUpdate(time, tempF, tempC, humidity, 1000.0);
    }
}
