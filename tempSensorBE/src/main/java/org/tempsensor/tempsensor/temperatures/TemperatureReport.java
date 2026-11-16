package org.tempsensor.tempsensor.temperatures;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class TemperatureReport {
    int temperature1;
    int temperature2;
    @JsonAlias({"timestamp", "time_stamp", "time"})
    String timeStamp;
}
