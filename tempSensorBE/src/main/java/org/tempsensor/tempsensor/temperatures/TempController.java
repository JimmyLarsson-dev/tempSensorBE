package org.tempsensor.tempsensor.temperatures;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temperatures")
public class TempController {

    TempService tempService;
    public TempController(TempService tempService) {
        this.tempService = tempService;
    }

    @GetMapping("/getLatest")
    public TempResponse[] getLatestTemps() {
        return tempService.getLatestTemps();
    }

    @GetMapping("/getAll")
    public TempResponse[] getAllTemps() {
        return tempService.getAllTemps();
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportTemps(@RequestBody TempReport tempReport) {
        return tempService.reportTemps(tempReport);
    }
}
