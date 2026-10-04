package org.example2.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MockMailSender implements MailSender {
    private final List<String> requests = new ArrayList<String>();

    public List<String> getRequests() {
        return requests;
    }

    @Override
    public void send(SimpleMailMessage mailMessage) {
        requests.add(mailMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage[] mailMessages) {
        for (SimpleMailMessage mailMessage : mailMessages) {
            send(mailMessage);
        }
    }
}
