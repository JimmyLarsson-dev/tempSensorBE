package org.tempsensor.tempsensor.temperatures;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HeaterDecider {

    // Antal varma mätningar som krävs innan elementet stängs av.
    private static final int WARM_READINGS_BEFORE_OFF = 20;

    private final TemperatureRepo tempRepo;
    private final HeaterCaller heaterCaller;

    public HeaterDecider(TemperatureRepo tempRepo, HeaterCaller heaterCaller) {
        this.tempRepo = tempRepo;
        this.heaterCaller = heaterCaller;
    }

    public boolean shouldHeaterBeOn(TemperatureReport report) {
        Optional<TemperatureEntity> latestEntry = tempRepo.findFirstByOrderByTimeStampDesc();
        if (latestEntry.isEmpty()) {
            return decideWithoutHistory();
        }
        if (latestEntry.get().isHeaterOn()) {
            return decideWhenHeaterIsOn(report);
        }
        return decideWhenHeaterIsOff(latestEntry.get());
    }

    // Inget i databasen ännu: slå på elementet.
    private boolean decideWithoutHistory() {
        return turnHeaterOn();
    }

    // Elementet är på: stäng av om det har varit över minTemp tillräckligt länge.
    private boolean decideWhenHeaterIsOn(TemperatureReport report) {
        if (hasBeenWarmLongEnough(report)) {
            return turnHeaterOff();
        }
        return true; // fortfarande på
    }

    // Elementet är av: slå på om temperaturen är under minTemp.
    private boolean decideWhenHeaterIsOff(TemperatureEntity latestEntry) {
        if (isBelowMin(latestEntry)) {
            return turnHeaterOn();
        }
        return false;
    }

    // Sant om de senaste 20 mätningarna i rad (den nya + de 19 senast sparade) alla är över minTemp.
    private boolean hasBeenWarmLongEnough(TemperatureReport report) {
        if (!isAboveMin(report)) {
            return false;
        }
        List<TemperatureEntity> previous = tempRepo.findTop19ByOrderByTimeStampDesc();
        return previous.size() == WARM_READINGS_BEFORE_OFF - 1
                && previous.stream().allMatch(HeaterDecider::isAboveMin);
    }

    private static boolean isAboveMin(TemperatureEntity entry) {
        return entry.getTemperature1() > entry.getMinTemperature();
    }

    private static boolean isAboveMin(TemperatureReport report) {
        return report.getTemperature1() > report.getMinTemperature();
    }

    private static boolean isBelowMin(TemperatureEntity entry) {
        return entry.getTemperature1() < entry.getMinTemperature();
    }

    private boolean turnHeaterOn() {
        heaterCaller.callHeater("on");
        return true;
    }

    private boolean turnHeaterOff() {
        heaterCaller.callHeater("off");
        return false;
    }
}
