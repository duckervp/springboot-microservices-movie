package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.domain.entity.Provider;
import com.duckervn.campaignservice.service.IVendorService;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class MailgunService implements IVendorService {
    @Override
    public Object send(Provider provider, Map<String, Object> recipient, String subject, String body) {
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
        if (provider.getSendMethod().equals(Constants.API)) {
            return sendViaAPI(provider, recipient, subject, body);
        }
        return sendViaSMTP(provider, recipient, subject, body);
    }

    private Object sendViaAPI(Provider provider, Map<String, Object> recipient, String subject, String body) {
        MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(provider.getApiKey())
                .createApi(MailgunMessagesApi.class);

        Message message = Message.builder()
                .from(provider.getSender())
                .to((String) recipient.get("email"))
                .subject(subject)
                .html(body)
                .build();

        return mailgunMessagesApi.sendMessage(provider.getApiDomainName(), message);
    }

    private Object sendViaSMTP(Provider provider, Map<String, Object> recipient, String subject, String body) {
        try {
            sendSMTPMail((String) recipient.get("email"), body, subject, provider);
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    private JavaMailSender getJavaMailSender(String host, int port, String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    private void sendSMTPMail(String email, String body, String subject, Provider provider) throws MessagingException {
        String senderName = Objects.toString(provider.getSender(), "");

        JavaMailSender mailSender = getJavaMailSender(provider.getHostname(), provider.getPort(),
                provider.getUsername(), provider.getPassword());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(senderName);
        helper.setTo(email);
        helper.setSubject(subject);

        helper.setText(body, true);

        mailSender.send(message);
    }
}
