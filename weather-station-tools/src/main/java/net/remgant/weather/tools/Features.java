package net.remgant.weather.tools;

import java.util.Collection;

public class Features {
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
    }

    public static class Properties {
        Property temperature;
        Property relativeHumidity;
        Property barometricPressure;

        public Property getTemperature() {
            return temperature;
        }

        public void setTemperature(Property temperature) {
            this.temperature = temperature;
        }

        public Property getRelativeHumidity() {
            return relativeHumidity;
        }

        public void setRelativeHumidity(Property relativeHumidity) {
            this.relativeHumidity = relativeHumidity;
        }

        public Property getBarometricPressure() {
            return barometricPressure;
        }

        public void setBarometricPressure(Property barometricPressure) {
            this.barometricPressure = barometricPressure;
        }

        @Override
        public String toString() {
            return "Properties{" +
                    "temperature=" + temperature +
                    ", relativeHumidity=" + relativeHumidity +
                    ", barometricPressure=" + barometricPressure +
                    '}';
        }
    }

    Collection<Properties> features;

    public Collection<Properties> getFeatures() {
        return features;
    }

    public void setFeatures(Collection<Properties> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Features{" +
                "features=" + features +
                '}';
    }
}
