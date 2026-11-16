package org.tempsensor.tempsensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TempSensorBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TempSensorBeApplication.class, args);
    }

}
