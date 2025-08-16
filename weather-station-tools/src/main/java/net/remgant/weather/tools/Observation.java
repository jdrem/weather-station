package net.remgant.weather.tools;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashMap;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

public class Observation  {
    static class Property {
       String unitCode;
       double value;
       String qualityControl;

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getQualityControl() {
            return qualityControl;
        }

        public void setQualityControl(String qualityControl) {
            this.qualityControl = qualityControl;
        }

        @Override
        public String toString() {
            return "Property{" +
                    "unitCode='" + unitCode + '\'' +
                    ", value=" + value +
                    ", qualityControl='" + qualityControl + '\'' +
                    '}';
        }
    }

    static class Properties {
        @JsonProperty("id")
        String id;
        String timestamp;
        Property temperature;
        Property barometricPressure;
        Property relativeHumidity;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Property getTemperature() {
            return temperature;
        }

        public void setTemperature(Property temperature) {
            this.temperature = temperature;
        }

        public Property getBarometricPressure() {
            return barometricPressure;
        }

        public void setBarometricPressure(Property barometricPressure) {
            this.barometricPressure = barometricPressure;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Property getRelativeHumidity() {
            return relativeHumidity;
        }

        public void setRelativeHumidity(Property relativeHumidity) {
            this.relativeHumidity = relativeHumidity;
        }

        @Override
        public String toString() {
            return "Properties{" +
                    "id='" + id + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", temperature=" + temperature +
                    ", barometricPressure=" + barometricPressure +
                    ", relativeHumidity=" + relativeHumidity +
                    '}';
        }
    }

    static class Features {
        String id;
        Properties properties;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String toString() {
            return "Features{" +
                    "id='" + id + '\'' +
                    ", properties=" + properties +
                    '}';
        }
    }

    Collection<Features> features;

    public Collection<Features> getFeatures() {
        return features;
    }

    public void setFeatures(Collection<Features> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "features=" + features +
                '}';
    }
}
