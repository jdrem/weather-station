package net.remgant.weather;

public class WeatherUpdate {
    private long timestamp;
    private double tempC;
    private double tempF;
    private double humidity;
    private double pressure;

    public WeatherUpdate() {
    }

    public WeatherUpdate(long timestamp, double tempF, double tempC, double humidity, double pressure) {
        this.timestamp = timestamp;
        this.tempF = tempF;
        this.tempC = tempC;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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
