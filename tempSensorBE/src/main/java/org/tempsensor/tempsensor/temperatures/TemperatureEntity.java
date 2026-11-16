package org.tempsensor.tempsensor.temperatures;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;
import lombok.Getter;
import java.util.Date;


@Getter
@Setter
@Entity
public class TemperatureEntity {
    @Id
    private String id;
    private int temperature1;
    private int temperature2;
    private boolean heaterShouldBeOn;
    private Date timeStamp;

    public TemperatureEntity(String id, int temperature1, int temperature2, boolean heaterShouldBeOn, Date timeStamp) {
        this.id = id;
        this.temperature1 = temperature1;
        this.temperature2 = temperature2;
        this.heaterShouldBeOn = heaterShouldBeOn;
        this.timeStamp = timeStamp;
    }
    public TemperatureEntity() {
    }
}
