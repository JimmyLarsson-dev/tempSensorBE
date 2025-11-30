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

    @GetMapping("/get")
    public TempResponse[] getTemps() {
        return tempService.getTemps();
    }
    @PostMapping("/report")
    public ResponseEntity<String> reportTemps(@RequestBody TempReport tempReport) {
        return tempService.reportTemps(tempReport);
    }
}
