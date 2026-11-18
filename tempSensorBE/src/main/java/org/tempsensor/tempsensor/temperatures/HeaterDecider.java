package org.tempsensor.tempsensor.temperatures;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HeaterDecider {

    private final TemperatureRepo tempRepo;
    private final HeaterCaller heaterCaller;
    public HeaterDecider(TemperatureRepo tempRepo, HeaterCaller heaterCaller) {
        this.tempRepo = tempRepo;
        this.heaterCaller = heaterCaller;
    }
    public boolean shouldHeaterBeOn(TemperatureReport report) {
        Optional<TemperatureEntity> latestTemp =  tempRepo.findFirstByOrderByTimeStampDesc();

        if (latestTemp.isPresent()) {
            List<TemperatureEntity> latest20 = tempRepo.findTop20ByOrderByTimeStampDesc();

            if(latestTemp.get().isHeaterOn()) {
                long okTemperatureForMinutes = latest20.stream().filter(x -> x.getTemperature1() > x.getMinTemperature()).count();
                //är den redan på? ISF:
                //har det varit över minTemp + 5 grader i 20 minuter?
                //isf stäng av
                //returnera false
                if(okTemperatureForMinutes > 19) {
                    heaterCaller.callHeater("off");
                    return false;
                }
            } else {
                //är den avstängd? isf:
                //är temp < minTemp?
                //isf sätt på element
                //returnera true
                if(latestTemp.get().getTemperature1() < latestTemp.get().getMinTemperature()) {
                    heaterCaller.callHeater("on");
                    return true;
                }
            }
        } else {
            //finns inget i databasen - vad gör vi då?
            return true;
        }










        return true;
    }
}