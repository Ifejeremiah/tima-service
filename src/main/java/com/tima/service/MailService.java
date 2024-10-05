package com.tima.service;

import com.tima.exception.EmailNotSentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailService {

    MailService() {
    }

    @Async
    public void sendMail(String recipient, String mail) {
        try {
            log.info("sent mail to = {}; body = {}", recipient, mail);
        } catch (Exception error) {
            throw new EmailNotSentException("Unable to send email: " + error);
        }
    }
}
