package com.example.webapplication.service;

import com.example.webapplication.model.Email;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private HttpServletRequest request;
    @Value("${spring.mail.username}")
    private String sender;
    @Async
    public CompletableFuture<Boolean> sendSimpleMail(Email details)
    {
        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            return CompletableFuture.completedFuture(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(false);
        }
    }
    @Async
    public CompletableFuture sendMailForVerify(String token, String email){
        Email mail=new Email();
        mail.setRecipient(email);
        String link=request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort()+"/api/v1/guest/verify/"+token;
        mail.setMsgBody("Please visit this page to verify your account: \n" +link +"\nThis code is valid for 20 minutes\n");
        mail.setSubject("Confirm your account");
        this.sendSimpleMail(mail);
        return null;
    }
    @Async
    public CompletableFuture sendMailForResetPassword(String token, String email){
        Email mail=new Email();
        mail.setRecipient(email);
        String link=request.getScheme() + "://" + request.getServerName()+":"+request.getServerPort()+"/api/v1/guest/verify/"+token;
        mail.setMsgBody("Please visit this page to reset your password: \n" +link +"\nThis code is valid for 20 minutes\n");
        mail.setSubject("Reset your password");
        this.sendSimpleMail(mail);
        return null;
    }
}
