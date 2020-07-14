package net.remgant.weather;

import net.remgant.weather.model.WeatherUpdate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class WeatherEventUpdateService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void publishWeatherUpdate(WeatherUpdate weatherUpdate) {
        publisher.publishEvent(new WeatherUpdateEvent(this, weatherUpdate));
    }
}
