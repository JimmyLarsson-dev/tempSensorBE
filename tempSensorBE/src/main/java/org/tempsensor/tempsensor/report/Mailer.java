package org.tempsensor.tempsensor.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tempsensor.tempsensor.temperatures.TemperatureEntity;
import org.tempsensor.tempsensor.temperatures.TemperatureRepo;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class Mailer {

    private static final long CHECK_INTERVAL_MS = 1000 * 60 * 5; // five minutes
    private static final Duration ALERT_COOLDOWN = Duration.ofMinutes(30);

    private final EmailService emailService;
    private final TemperatureRepo temperatureRepo;

    // When the last alert mail was actually sent; null = never.
    private Instant lastAlertSentAt;

    public Mailer(EmailService emailService, TemperatureRepo temperatureRepo) {
        this.emailService = emailService;
        this.temperatureRepo = temperatureRepo;
    }

    @Scheduled(fixedRate = CHECK_INTERVAL_MS)
    public void run() {
        try {
            checkTemperatures();
        } catch (Exception e) {
            // Catch everything so one failed check (e.g. DB down) is logged
            // instead of silently stopping future runs.
            log.error("Temperature check failed", e);
        }
    }

    private void checkTemperatures() {
        List<TemperatureEntity> temps = temperatureRepo.findAll();
        //om db är tom
        if (temps.isEmpty()) {
            sendNoContentWarning();
            return; // ends only this check; the next one runs in five minutes
        }
        List<TemperatureEntity> sortedByDate = temps
                .stream()
                .sorted((t1, t2) -> t2.getTimeStamp().compareTo(t1.getTimeStamp()))
                .toList();
        Date lastTemp = sortedByDate.getFirst().getTimeStamp();
        Date now = new Date();
        long diffInMillies = Math.abs(now.getTime() - lastTemp.getTime());
        //om db har inte uppdaterats de senast 15 minuterna
        if (diffInMillies > 1000 * 60 * 15) {
            sendDatabaseNotUpdatedWarning(diffInMillies / (1000 * 60));
        }
        //om temperaturen har varit för låg i 10 minuter
    }

    private void sendDatabaseNotUpdatedWarning(long ageOfLastTemp) {
        String content = "It seems like the database has not been updated for " + ageOfLastTemp + " minutes.";
        sendTheMail(content);
    }

    private void sendNoContentWarning() {
        String content = "There seems to be no content in the database. What happened?";
        sendTheMail(content);
    }

    private void sendTheMail(String content) {
        //om vi har skickat ett mail de senaste 30 minuterna så skickar vi inget nytt
        Instant now = Instant.now();
        if (lastAlertSentAt != null && now.isBefore(lastAlertSentAt.plus(ALERT_COOLDOWN))) {
            log.info("Alert suppressed, last alert was sent at {}: {}", lastAlertSentAt, content);
            return;
        }
        EmailDetails emailDetails = new EmailDetails(
                "jimmy.larsson@europe.com",
                content,
                "This is a test email",
                ""
        );
        String result = emailService.sendMail(emailDetails);
        // Only start the cooldown if the mail really went out, so a failed
        // send is retried on the next check instead of 30 minutes later.
        if (EmailServiceImpl.SUCCESS_MESSAGE.equals(result)) {
            lastAlertSentAt = now;
        } else {
            log.warn("Alert mail could not be sent: {}", result);
        }
    }
}
