package net.remgant.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.remgant.weather.model.WeatherUpdate;
import net.remgant.weather.gson.InstantAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UpdateReceiver {
    private final static Logger log = LoggerFactory.getLogger(UpdateReceiver.class);
    private final WeatherEventUpdateService weatherEventUpdateService;
    private final Gson gson;

    public UpdateReceiver(WeatherEventUpdateService weatherEventUpdateService) {
        this.weatherEventUpdateService = weatherEventUpdateService;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Instant.class, new InstantAdapter());
        this.gson = builder.create();
    }

    @SuppressWarnings("unused")
    public void receiveMessage(String message) {
        WeatherUpdate weatherUpdate = gson.fromJson(message, WeatherUpdate.class);
        log.debug("Received update {}", weatherUpdate);
        weatherEventUpdateService.publishWeatherUpdate(weatherUpdate);
    }

}
