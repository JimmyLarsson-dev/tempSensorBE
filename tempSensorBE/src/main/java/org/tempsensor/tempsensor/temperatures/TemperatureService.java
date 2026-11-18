package org.tempsensor.tempsensor.temperatures;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tempsensor.tempsensor.report.EmailDetails;
import org.tempsensor.tempsensor.report.EmailServiceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
public class TemperatureService {

    TemperatureRepo temperatureRepo;
    EmailServiceImpl emailService;
    HeaterDecider heaterDecider;
    public TemperatureService(TemperatureRepo temperatureRepo, EmailServiceImpl emailService, HeaterDecider heaterDecider) {
        this.heaterDecider = heaterDecider;
        this.temperatureRepo = temperatureRepo;
        this.emailService = emailService;
    }

    public TemperatureResponse[] getLatestTemps() {
        return temperatureRepo.findTop20ByOrderByTimeStampDesc()
                .stream()
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
            timeStamp = parseTimeStamp(temperatureReport.getTimeStamp());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid timeStamp, expected ISO-8601 like 2026-11-16T12:00:00Z");
        }
        boolean turnHeaterOn = heaterDecider.shouldHeaterBeOn(temperatureReport);
        TemperatureEntity temperatureEntity = TemperatureEntity.builder()
                .id(UUID.randomUUID().toString())
                .temperature1(temperatureReport.getTemperature1())
                .temperature2(temperatureReport.getTemperature2())
                .minTemperature(temperatureReport.getMinTemperature())
                .heaterOn(turnHeaterOn)
                .timeStamp(timeStamp)
                .build();
        temperatureRepo.save(temperatureEntity);
        return ResponseEntity.ok("Temps reported");
    }

    /**
     * No timestamp -> now.
     * With zone/offset ("2026-11-16T12:00:00Z", "2026-11-16T13:00:00+01:00") -> that exact moment.
     * Without zone ("2026-11-16T13:00:00") -> interpreted as the server's local time.
     */
    static Instant parseTimeStamp(String raw) {
        if (raw == null || raw.isBlank()) {
            return Instant.now();
        }
        try {
            return OffsetDateTime.parse(raw).toInstant();
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(raw).atZone(ZoneId.systemDefault()).toInstant();
        }
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
