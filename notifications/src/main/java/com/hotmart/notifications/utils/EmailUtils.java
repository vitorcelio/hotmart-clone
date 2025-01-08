package com.hotmart.notifications.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailUtils {

    private final Environment env;

    public MimeMessageHelper createConfigEmail(MimeMessage mimeMessage, String emailTo, String title) throws MessagingException, UnsupportedEncodingException {
        String mailFrom = env.getProperty("spring.mail.username");
        String mailName = env.getProperty("mail.from.name");

        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setFrom(new InternetAddress(mailFrom, mailName));
        email.setTo(emailTo);
        email.setSubject(title);
        return email;
    }

}
