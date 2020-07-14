package net.remgant.weather;

import net.remgant.weather.model.WeatherUpdate;
import org.springframework.context.ApplicationEvent;

public class WeatherUpdateEvent extends ApplicationEvent {
    private final WeatherUpdate weatherUpdate;
    public WeatherUpdateEvent(Object source, WeatherUpdate weatherUpdate) {
        super(source);
        this.weatherUpdate = weatherUpdate;
    }

    public WeatherUpdate getWeatherUpdate() {
        return weatherUpdate;
    }
}
