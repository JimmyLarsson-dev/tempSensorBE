package org.tempsensor.tempsensor.report;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@EnableAsync
public class Mailer {

    private final EmailService emailService;

    public Mailer(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void run(){
        String standardBody = "This is a test email. \n" +
                "It contains a lot of text. \n" +
                "It is very long. \n" +
                "but it is not too long.";
        boolean sendMail = true;
        while (true){
            if(sendMail) {
                System.out.println("Sending email");

                EmailDetails emailDetails = new EmailDetails(
                        "jimmy.larsson@europe.com",
                        standardBody,
                        "This is a test email",
                        ""
                );

                emailService.sendMail(emailDetails);
//                EmailServiceImpl emailService = new EmailServiceImpl(new JavaMailSenderImpl());
//                emailService.sendMail(emailDetails);
                try {
                    Thread.sleep(60000); //One-minute sleep
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
