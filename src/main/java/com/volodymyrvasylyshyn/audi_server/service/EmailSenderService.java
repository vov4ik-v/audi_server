package com.volodymyrvasylyshyn.audi_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailSenderService {


    @Autowired
    private JavaMailSender javaMailSender;


    public void sendMail(String toMail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toMail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);

    }

    public void sendMailToManyPerson(List<String> emailList, String subject, String message) throws MessagingException {
        String[] emails =listToArray(emailList);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(emails);
        messageHelper.setSubject(subject);
        messageHelper.setText(message);
        javaMailSender.send(mimeMessage);

    }
    private String[] listToArray(List<String> emailList){
        String[] emails = new String[emailList.size()];
        for (int i = 0; i<emailList.size();i++){
            emails[i] = emailList.get(i);
        }
        return emails;

    }
}
