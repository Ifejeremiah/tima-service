package com.tima.service;

import com.tima.exception.EmailNotSentException;
import com.tima.model.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailService {
    JavaMailSender mailSender;
    @Value("${SPRING.MAIL.USERNAME}")
    private String mailer;

    @Autowired
    MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String recipient, Mail mail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(mailer, mail.getSender());
            helper.setTo(recipient);
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContext(), true);

            mailSender.send(message);
        } catch (Exception error) {
            throw new EmailNotSentException("Unable to send email: " + error);
        }
    }
}
