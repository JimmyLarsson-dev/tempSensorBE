package org.tempsensor.tempsensor.temp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TempResponse {
    int temp1;
    int temp2;
    int currentThreshold;
    Date date;
    boolean heaterIsOn;
}
