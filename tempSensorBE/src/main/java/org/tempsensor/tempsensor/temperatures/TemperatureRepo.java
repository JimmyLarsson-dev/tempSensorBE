package org.tempsensor.tempsensor.temperatures;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TemperatureRepo extends JpaRepository<TemperatureEntity, String> {

    // Newest row, or empty if the table is empty.
    Optional<TemperatureEntity> findFirstByOrderByTimeStampDesc();

    // The 20 newest rows, newest first.
    List<TemperatureEntity> findTop20ByOrderByTimeStampDesc();

    // The 19 newest rows, newest first (used together with the incoming report = 20 readings).
    List<TemperatureEntity> findTop19ByOrderByTimeStampDesc();
}
