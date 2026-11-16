package org.tempsensor.tempsensor.temperatures;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureRepo extends JpaRepository<TemperatureEntity, String> {
}
