package com.tima.util;

import com.tima.model.Mail;

public class MailUtil {
    public static Mail constructOTPMail(String otp) {
        Mail mail = new Mail();
        mail.setSubject("Verify your email address");
        mail.setContext(String.format("Verification Code: <b>%s</b>. This code will expire 5 minutes after it was sent.", otp));
        return mail;
    }
}
