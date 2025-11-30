package org.tempsensor.tempsensor.temp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController()
@RequestMapping("/temp")
public class TempController {

    @GetMapping("/getTemps")
    public TempResponse[] getTemps(){
        TempResponse[] temps = new TempResponse[2];
        TempResponse temp0 = new TempResponse(
                15,
                14,
                10,
                new Date(),
                true
        );
        TempResponse temp1 = new TempResponse(
                13,
                13,
                10,
                new Date(),
                true
        );
        temps[0] = temp0;
        temps[1] = temp1;

        return temps;
    }


}
