package com.umut.project.database.mywebservice.email;

import com.umut.project.database.mywebservice.configuration.EmailProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Service
public class EmailService {

    private JavaMailSenderImpl mailSender;
    private final EmailProperties emailProperties;

    public EmailService(EmailProperties emailProperties)
    {
        this.emailProperties = emailProperties;
    }

    public void sendMail(String email, String token)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("umutfitness@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Umut Fitness Aktivasyon İşlemi");
        mailMessage.setText("localhost:5173/activation/" + token);
        this.mailSender.send(mailMessage);
    }

    @PostConstruct
    private void init()
    {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailProperties.getHost());
        mailSender.setUsername(emailProperties.getUsername());
        mailSender.setPassword(emailProperties.getPassword());
        mailSender.setPort(emailProperties.getPort());
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
    }

}
