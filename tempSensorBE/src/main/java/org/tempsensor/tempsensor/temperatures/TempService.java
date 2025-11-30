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

    public TempResponse[] getTemps() {
//        TempResponse[] temps = new TempResponse[2];
//        TempResponse temp1 = new TempResponse(20, 25, true, new java.util.Date());
//        TempResponse temp2 = new TempResponse(22, 23, false, new java.util.Date());
//        temps[0] = temp1;
//        temps[1] = temp2;

        return tempRepo.findAll()
                .stream()
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
}
