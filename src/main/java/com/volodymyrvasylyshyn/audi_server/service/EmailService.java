package com.volodymyrvasylyshyn.audi_server.service;

import com.volodymyrvasylyshyn.audi_server.exeptions.EmailNotFoundExeption;
import com.volodymyrvasylyshyn.audi_server.model.Email;
import com.volodymyrvasylyshyn.audi_server.repository.EmailRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public void addEmail(Email email) {
        emailRepository.save(email);
    }

    public List<Email> getAll() {
        return emailRepository.findAll();
    }
    public void deleteEmail(Integer id){
        Email email = emailRepository.findById(id).orElseThrow(() ->new EmailNotFoundExeption("email not found"));
        emailRepository.delete(email);

    }

    public List<String> getAllEmails(List<Email> emailList) {
        List<String> emails = new ArrayList<>();
        for (Email email : emailList) {
            emails.add(email.getEmail());
        }
        return emails;
    }
}
