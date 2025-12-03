package org.tempsensor.tempsensor.temperatures;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class TempService {

    TempRepo tempRepo;
    public TempService(TempRepo tempRepo) {
        this.tempRepo = tempRepo;
    }

    public TempResponse[] getLatestTemps() {
        return tempRepo.findAll()
                .stream()
                .sorted((t1, t2) -> t2.getTimeStamp().compareTo(t1.getTimeStamp()))
                .limit(10)
                .map(t -> new TempResponse(
                        t.getTemp1(),
                        t.getTemp2(),
                        true,
                        t.getTimeStamp()))
                .toArray(temp -> new TempResponse[temp]);
    }

    public ResponseEntity<String> reportTemps(TempReport tempReport) {
        tempRepo.save(
                new TempDoc(
                UUID.randomUUID().toString(),
                tempReport.getTemp1(),
                tempReport.getTemp2(),
                true,
                Date.from(Instant.parse(tempReport.getTimeStamp()))
                ));
        return ResponseEntity.ok("Temps reported");
    }

    public TempResponse[] getAllTemps() {
        return tempRepo.findAll()
                .stream()
                .map(t -> new TempResponse(
                        t.getTemp1(),
                        t.getTemp2(),
                        true,
                        t.getTimeStamp()))
                .toArray(temp -> new TempResponse[temp]);
    }
}
