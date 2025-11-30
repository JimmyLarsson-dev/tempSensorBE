package org.tempsensor.tempsensor.temperatures;

import lombok.Data;

import java.util.Date;

@Data
public class TempReport {
    int temp1;
    int temp2;
    String timeStamp;
}
