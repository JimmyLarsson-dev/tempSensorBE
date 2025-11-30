package org.tempsensor.tempsensor.temperatures;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TempResponse {
    int temp1;
    int temp2;
    boolean heaterShouldBeOn;
    Date timeStamp;
}
