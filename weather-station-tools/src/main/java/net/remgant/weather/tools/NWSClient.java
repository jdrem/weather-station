package net.remgant.weather.tools;

import feign.Param;
import feign.RequestLine;

import java.util.Collection;

public interface NWSClient {

    @RequestLine("GET /stations/{stationId}/observations/latest")
    Features getLatestObservation(@Param("stationId") String stationId);

    @RequestLine("GET /stations/{stationId}/observations")
    Observation getAllAvailableObservations(@Param("stationId") String stationId);
}
