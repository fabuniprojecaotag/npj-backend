package com.uniprojecao.fabrica.gprojuridico.services;

import com.google.appengine.api.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private MailService mailService;

    private Properties props = new Properties();
    private Session session = Session.getDefaultInstance(props, null);

    public void sendEmail() {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("user@example.com", "Mr. User"));
            message.setSubject("Your Example.com account has been activated");
            message.setText("This is a test");
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            // ...
        }
    }

    public void enviarEmail(String destinatario, String assunto, String corpo) throws IOException {
        var message = new MailService.Message();
        message.setSender("");
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setTextBody(corpo);

        mailService.send(message);
    }
}
