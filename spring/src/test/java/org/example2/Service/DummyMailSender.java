package org.example2.Service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {
    @Override
    public void send(SimpleMailMessage mailMessage) {
    }

    @Override
    public void send(SimpleMailMessage[] mailMessages) {
    }
}
