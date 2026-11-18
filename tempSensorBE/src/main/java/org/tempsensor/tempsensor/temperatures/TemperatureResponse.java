package org.tempsensor.tempsensor.temperatures;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class TemperatureResponse {
    int temperature1;
    int temperature2;
    boolean heaterShouldBeOn;
    Instant timeStamp; // serialized as ISO-8601, e.g. "2026-11-16T12:00:00Z"
}
