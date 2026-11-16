package org.tempsensor.tempsensor.temperatures;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tempsensor.tempsensor.report.EmailDetails;
import org.tempsensor.tempsensor.report.EmailServiceImpl;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.UUID;

@Service
public class TemperatureService {

    TemperatureRepo temperatureRepo;
    EmailServiceImpl emailService;
    public TemperatureService(TemperatureRepo temperatureRepo, EmailServiceImpl emailService) {

        this.temperatureRepo = temperatureRepo;
        this.emailService = emailService;
    }

    public TemperatureResponse[] getLatestTemps() {
        return temperatureRepo.findAll()
                .stream()
                .sorted((t1, t2) -> t2.getTimeStamp().compareTo(t1.getTimeStamp()))
                .limit(10)
                .map(t -> new TemperatureResponse(
                        t.getTemperature1(),
                        t.getTemperature2(),
                        true,
                        t.getTimeStamp()))
                .toArray(temp -> new TemperatureResponse[temp]);
    }

    public ResponseEntity<String> reportTemperatures(TemperatureReport temperatureReport) {
        Instant timeStamp;
        try {
            timeStamp = temperatureReport.getTimeStamp() == null
                    ? Instant.now()
                    : Instant.parse(temperatureReport.getTimeStamp());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid timeStamp, expected ISO-8601 like 2026-11-16T12:00:00Z");
        }

        temperatureRepo.save(
                new TemperatureEntity(
                UUID.randomUUID().toString(),
                temperatureReport.getTemperature1(),
                temperatureReport.getTemperature2(),
                true,
                Date.from(timeStamp)
                ));
        return ResponseEntity.ok("Temps reported");
    }

    public TemperatureResponse[] getAllTemps() {
        return temperatureRepo.findAll()
                .stream()
                .map(t -> new TemperatureResponse(
                        t.getTemperature1(),
                        t.getTemperature2(),
                        true,
                        t.getTimeStamp()))
                .toArray(temp -> new TemperatureResponse[temp]);
    }

    public String sendMail() {

        EmailDetails emailDetails = new EmailDetails(
                "jimmy.larsson@europe.com",
                "This is a test email body bodybodybodybodybodybody",
                "This is a test email subject",
                "");
        return emailService.sendMail(emailDetails);
    }
}
