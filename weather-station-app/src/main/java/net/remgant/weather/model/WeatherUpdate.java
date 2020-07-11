package net.remgant.weather.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.Instant;

@Entity
public class WeatherUpdate {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private Instant timestamp;
    private double tempC;
    private double tempF;
    private double humidity;
    private double pressure;

    public WeatherUpdate() {
    }

    public WeatherUpdate(Instant timestamp, double tempF, double tempC, double humidity, double pressure) {
        this.timestamp = timestamp;
        this.tempF = tempF;
        this.tempC = tempC;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public double getTempF() {
        return tempF;
    }

    public void setTempF(double tempF) {
        this.tempF = tempF;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "Update{" +
                "timestamp=" + timestamp +
                ", tempC=" + tempC +
                ", tempF=" + tempF +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                '}';
    }
}
