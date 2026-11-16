package org.tempsensor.tempsensor.temperatures;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temperatures")
public class TemperatureController {

    TemperatureService temperatureService;
    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @PostMapping("/testMail")
    public String testMail() {
        return temperatureService.sendMail();
    }

    @GetMapping("/getLatest")
    public TemperatureResponse[] getLatestTemperaturess() {
        return temperatureService.getLatestTemps();
    }

    @GetMapping("/getAll")
    public TemperatureResponse[] getAllTemperatures() {
        return temperatureService.getAllTemps();
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportTemperatures(@RequestBody TemperatureReport temperatureReport) {
        return temperatureService.reportTemperatures(temperatureReport);
    }
}
