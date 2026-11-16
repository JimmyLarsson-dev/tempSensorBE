package org.tempsensor.tempsensor.temperatures;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TemperatureResponse {
    int temperature1;
    int temperature2;
    boolean heaterShouldBeOn;
    Date timeStamp;
}
