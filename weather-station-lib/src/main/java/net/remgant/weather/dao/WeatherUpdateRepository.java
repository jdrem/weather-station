package net.remgant.weather.dao;

import net.remgant.weather.model.WeatherUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WeatherUpdateRepository  extends JpaRepository<WeatherUpdate, Long>, JpaSpecificationExecutor<WeatherUpdate> {
}
