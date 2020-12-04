package net.remgant.weather.tools;

import net.remgant.weather.model.WeatherUpdate;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateFromFile {
    String dateFileName;
    String hostName;
    String userName;
    String password;

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println("Usage: UpdateFromFile fileName host user password");
            System.exit(1);
        }
        new UpdateFromFile(args[0], args[1], args[2], args[3]).run();
    }

    public UpdateFromFile(String dateFileName, String hostName, String userName, String password) {
        this.dateFileName = dateFileName;
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
    }

    public void run() throws IOException {
        Path path = FileSystems.getDefault().getPath(dateFileName);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String auth = userName + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        URI deleteUri;
        try {
            deleteUri = new URI(String.format("http://%s/api/deleteAll", hostName));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        RequestEntity<Void> deleteAllRequest = RequestEntity.get(deleteUri)
                .headers(headers)
                .build();
        restTemplate.exchange(deleteAllRequest, Void.class);

        URI updateUri;
        try {
            updateUri = new URI(String.format("http://%s/api/update", hostName));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // 2020-07-12 9:54 AM	76 F	65 F	69 %	WSW	16 mph	21 mph	28.62 in	0.0 in	Cloudy
        Pattern p = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})\\s+(\\d+):(\\d{2})\\s+(AM|PM)\\s+(\\d+)\\s+F.*\\s(\\d+)\\s%.*mph\\s+(\\d+\\.\\d+)\\sin.*");

        Files.lines(path).forEach(l -> {
            Matcher m = p.matcher(l);
            if (m.matches()) {
                WeatherUpdate weatherUpdate = new WeatherUpdate();
                System.out.printf("%s %s %s %s %s %s %s %s %s%n", m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), m.group(7), m.group(8), m.group(9));
                int hour = Integer.parseInt(m.group(4));
                String ampm = m.group(6);
                if (hour == 12 && ampm.equals("AM"))
                    hour = 0;
                if (ampm.equals("PM"))
                    hour += 12;
                hour %= 24;
                LocalDate localDate = LocalDate.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
                LocalTime localTime = LocalTime.of(hour, Integer.parseInt(m.group(5)));
                ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(localDate, localTime), ZoneId.of("UTC"));
                weatherUpdate.setTimestamp(zdt.toInstant());

                weatherUpdate.setTempF(Integer.parseInt(m.group(7)));
                weatherUpdate.setTempC((weatherUpdate.getTempF() - 32.0) * 5.0 / 9.0);
                weatherUpdate.setHumidity(Integer.parseInt(m.group(8)));
                weatherUpdate.setPressure(Double.parseDouble(m.group(9)) * 33.8639);

                System.out.println(weatherUpdate);

                RequestEntity<WeatherUpdate> weatherUpdateRequest = RequestEntity.post(updateUri)
                        .headers(headers)
                        .body(weatherUpdate);
                restTemplate.exchange(weatherUpdateRequest, Void.class);
            }
        });
    }

}
