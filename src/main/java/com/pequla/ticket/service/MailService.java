package com.pequla.ticket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    private String username;
    private final JavaMailSender sender;

    public void send(String to, String body, String subject) {
        new Thread(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(username);
            message.setTo(to);
            message.setText(body);
            message.setSubject(subject);

            sender.send(message);
            log.info("Mail \"" + subject + "\" sent to " + to);
            log.info("Body: " + body);
        }, "MailSender-" + to).start();
    }
}
