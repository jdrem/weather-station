package net.remgant.weather.tools;

import net.remgant.weather.model.WeatherUpdate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class UpdateFromNWS {

    public static void main(String args[]) {
        new UpdateFromNWS().run();
    }
    public void run() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","weather app (weather@remgant.net)");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        for (int i=0; i< 100; i++ ) {
            ResponseEntity<Map> response = restTemplate.exchange("https://api.weather.gov//stations/KFIT/observations/current",
                    HttpMethod.GET, entity, Map.class);
            System.out.println(response);
            Map map = response.getBody();
            map = (Map) map.get("properties");
            Map subMap = (Map) map.get("temperature");
            double tempC = (double) subMap.get("value");
            subMap = (Map) map.get("barometricPressure");
            int pressure = (int) subMap.get("value");
            subMap = (Map) map.get("relativeHumidity");
            double humidity = (double) subMap.get("value");

            WeatherUpdate weatherUpdate = new WeatherUpdate();
            weatherUpdate.setTempC(tempC);
            weatherUpdate.setTempF(tempC * 9.0 / 5.0 + 32.0);
            weatherUpdate.setHumidity(humidity);
            weatherUpdate.setPressure((double) pressure / 100.0);
            restTemplate.postForEntity("http://localhost:9099/api/update", weatherUpdate, String.class);
            try {
                Thread.sleep(5*60000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
