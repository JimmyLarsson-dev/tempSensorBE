package org.tempsensor.tempsensor.report;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {
    private final Mailer mailer;

    public StartupRunner(Mailer mailer) {
        this.mailer = mailer;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mailer.run();
    }
}
