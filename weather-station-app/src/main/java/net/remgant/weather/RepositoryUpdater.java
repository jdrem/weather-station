package net.remgant.weather;

import net.remgant.weather.dao.WeatherUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class RepositoryUpdater implements ApplicationListener<WeatherUpdateEvent> {
    final static private Logger log = LoggerFactory.getLogger(RepositoryUpdater.class);
    final WeatherUpdateRepository repository;

    public RepositoryUpdater(WeatherUpdateRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(WeatherUpdateEvent weatherUpdateEvent) {
        log.info("Got event for {}", weatherUpdateEvent.getWeatherUpdate());
        repository.save(weatherUpdateEvent.getWeatherUpdate());
    }
}
