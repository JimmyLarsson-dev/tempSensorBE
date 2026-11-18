package org.tempsensor.tempsensor.temperatures;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TemperatureRepo extends JpaRepository<TemperatureEntity, String> {

    // Newest row, or empty if the table is empty.
    Optional<TemperatureEntity> findFirstByOrderByTimeStampDesc();

    // The ten newest rows, newest first.
    List<TemperatureEntity> findTop20ByOrderByTimeStampDesc();
}
