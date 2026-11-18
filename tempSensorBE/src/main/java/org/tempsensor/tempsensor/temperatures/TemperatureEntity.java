package org.tempsensor.tempsensor.temperatures;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@Entity
@Builder
public class TemperatureEntity {
    @Id
    private String id;
    private int temperature1;
    private int temperature2;
    private int minTemperature;
    private boolean heaterOn;
    // A point in time (date + time of day, UTC). Stored as "timestamp with time zone" in Postgres.
    private Instant timeStamp;

    public TemperatureEntity(String id, int temperature1, int temperature2, int minTemperature, boolean heaterOn, Instant timeStamp) {
        this.id = id;
        this.temperature1 = temperature1;
        this.temperature2 = temperature2;
        this.minTemperature = minTemperature;
        this.heaterOn = heaterOn;
        this.timeStamp = timeStamp;
    }
    public TemperatureEntity() {
    }
}
